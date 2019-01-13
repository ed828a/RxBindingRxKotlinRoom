package com.example.edward.rxjavaroomexercise

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userListView.adapter = adapter

        val databaseDao = UserDatabase.getDatabase(this).userDoa()
        val d1 = RxView.clicks(buttonAddUser)
            .observeOn(Schedulers.io())
            .map {
                val userName = editUserName.text.toString().trim()
                val contact = editContact.text.toString().trim()
                User(name = userName, contact = contact)
            }
            .map { user -> databaseDao.addUser(user) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                Toast.makeText(this, "User: $it is added into the database", Toast.LENGTH_SHORT).show()
                editUserName.text.clear()
                editContact.text.clear()
            }

        val d = databaseDao.getAllUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {list ->
                    adapter.showUser(list)
                    adapter.notifyDataSetChanged()
                },
                {error ->
                    Log.d(TAG, "getAllUsers() return error: ${error.message}")
                },
                {
                    Toast.makeText(this, "the transmission is done!", Toast.LENGTH_SHORT).show()
                }

            )

        val d2 = RxTextView.editorActions(editUserName)
            .observeOn(Schedulers.computation())
            .doOnNext {
                Log.d(TAG, "UserNameEdit hit enter! $it ")
            }
            .debounce(500, TimeUnit.MILLISECONDS)
            .flatMapMaybe {event ->
                val name = editUserName.text.toString().trim()
                databaseDao.getUserByName(name)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {list ->
                    adapter.showUser(list)
                    adapter.notifyDataSetChanged()
                },
                {error ->
                    Toast.makeText(this, "No such user! ${error.message} ", Toast.LENGTH_SHORT).show()
                }
            )

        val d3 = RxTextView.editorActions(editContact)
            .observeOn(Schedulers.computation())
            .doOnNext {
                Log.d(TAG, "contactEdit hit enter! $it ")
            }
            .debounce(500, TimeUnit.MILLISECONDS)
            .flatMapSingle { event ->
                val contact = editContact.text.toString().trim()
                databaseDao.getUserByContact(contact)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {list ->
//                    val list = arrayListOf<User>()

                    adapter.showUser(list)
                    adapter.notifyDataSetChanged()
                },
                {error ->
                    Toast.makeText(this, "No such user! ${error.message} ", Toast.LENGTH_SHORT).show()
                },
                {
                    Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show()
                }
            )


        compositeDisposable.addAll(d, d1, d2, d3)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()

    }

    companion object {
        const val TAG = "MainActivity"
    }
}
