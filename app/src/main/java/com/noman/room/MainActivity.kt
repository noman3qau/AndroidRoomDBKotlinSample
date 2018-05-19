package com.noman.room

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.noman.room.database.UserDao
import com.noman.room.model.User
import com.noman.room.ui.UserItemAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.arch.persistence.room.Room
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import com.noman.room.database.AppRoomDatabase
import com.noman.room.model.Product
import com.noman.room.ui.ProductItemAdapter
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe.subscribe
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_product_item.*
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

/**
 * MainActivity class where all ui field are shown to database operation in table
 * Here we have two table one "user" and other "product"
 * "product" table to child table to parent table "user"
 */
class MainActivity : AppCompatActivity() {

    /**
     * A composite object that handle refernces of Observers and later  in onDestory Activity this is disposed all objects
     */
    var disposable: CompositeDisposable = CompositeDisposable()

    /**
     * Currently selected user that is select from user list while clicking in item in user list
     */
    var currentUser: User? = null

    /**
     * A interface class that send callback to activity when click in user list from user list adapter to activity
     */
    interface OnItemSelectListener {
        public fun onItemSelect(user: User)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)

        buttonAddUser.setOnClickListener {
            addUser()
        }

        buttonAddProduct.setOnClickListener {
            addProduct();
        }

        deleteChioce.setOnCheckedChangeListener { _group, checkedId ->
            if (checkedId == R.id.isUser) {
                nameDelete.hint = "User Name"
            } else {
                nameDelete.hint = "Product Name"
            }
        }

        buttonDelete.setOnClickListener {

            if (deleteChioce.checkedRadioButtonId == R.id.isUser) {
                deleteUserByName()
            } else if (deleteChioce.checkedRadioButtonId == R.id.isProduct) {
                deleteProductByName()
            } else {
                Toast.makeText(this, "Please selete what you want to delete.", Toast.LENGTH_SHORT).show()
            }
        }

        headUsers.setOnClickListener { showAllUsersToUI() }
        headProducts.setOnClickListener { showAllProductsToUI() }

        /**
         * First time shows all user to ui list if any user exist in database
         */
        showAllUsersToUI()
    }

    private fun addUser() {

        disposable.add(Single.fromCallable {
            addUserToDB()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _result -> showAllUsersToUI() }))

    }

    /**
     * Below all methods show their purpose by name
     */

    private fun addProduct() {
        if (currentUser == null) {
            Toast.makeText(this, "Please select user from user's list.", Toast.LENGTH_SHORT).show()
        } else {
            disposable.add(Single.fromCallable {
                addProductToDB(currentUser!!)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ _result -> showAllProductsToUI() }))
        }
    }

    private fun addUserToDB(): Unit? {
        return AppRoomDatabase.getInstance(this)
                ?.getUserDao()
                ?.intert(User(numberAdd.text.toString(), nameAdd.text.toString(), emailAdd.text.toString()))
    }

    private fun addProductToDB(userNum: User): Unit? {
        var price: Int = 0
        try {
            price = productPriceAdd.text.toString().toInt()
        } catch (e: Exception) {

        }
        return AppRoomDatabase.getInstance(this)
                ?.getProductDao()
                ?.insert(Product(userNum.number, System.currentTimeMillis(), productNameAdd.text.toString(), productColorAdd.text.toString(), price))
    }

    private fun deleteUserByName() {

        disposable.add(Single.fromCallable {
            deleteUser()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _result -> showAllUsersToUI() }))

    }

    private fun deleteProductByName() {

        disposable.add(Single.fromCallable {
            deleteProduct()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _result -> showAllProductsToUI() }))

    }

    private fun deleteUser() {
        if (nameDelete.text.toString() == "all") {
            AppRoomDatabase.getInstance(this)?.getUserDao()
                    ?.deleteAll()
        } else {
            AppRoomDatabase.getInstance(this)?.getUserDao()
                    ?.deleteUserByName(nameDelete.text.toString())
        }
    }

    private fun deleteProduct() {
        if (nameDelete.text.toString() == "all") {
            AppRoomDatabase.getInstance(this)?.getProductDao()
                    ?.deleteAll()
        } else {
            AppRoomDatabase.getInstance(this)?.getProductDao()
                    ?.deleteProductByName(nameDelete.text.toString())
        }
    }

    private fun showAllUsersToUI() {
        disposable.add(Single.fromCallable {
            getAllUsers()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userList ->
                    showUsersInList(userList)
                }))
    }

    private fun showAllProductsToUI() {
        disposable.add(Single.fromCallable {
            getAllProducts()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ productList ->
                    showProductsInList(productList)
                }))
    }

    private fun showUsersInList(userList: List<User>) {
        recyclerViewUsers.adapter = UserItemAdapter(userList, object : OnItemSelectListener {
            override fun onItemSelect(user: User) {
                currentUser = user
                getAllProductsByNumber()
            }
        }
        )
        var i: Int = 0
        i++
    }

    private fun getAllProductsByNumber() {
        if (currentUser == null) {
            Toast.makeText(this, "Please select user from user's list.", Toast.LENGTH_SHORT).show()
        } else {
            Single.fromCallable {
                getDBProductsByNumber(currentUser!!)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ productList ->
                        showProductsInList(productList)
                    })
        }
    }

    private fun getDBProductsByNumber(usernum: User): List<Product>? {
        val allProductList = AppRoomDatabase
                .getInstance(this@MainActivity)
                ?.getProductDao()
                ?.getProductByUserNumber(usernum.number)
        return allProductList
    }

    private fun showProductsInList(productList: List<Product>) {
        recyclerViewProducts.adapter = ProductItemAdapter(productList)
        var i: Int = 0
        i++
    }

    private fun getAllUsers(): List<User>? {
        val allUserList = AppRoomDatabase
                .getInstance(this@MainActivity)
                ?.getUserDao()
                ?.getAllUsers()
        return allUserList
    }

    private fun getAllProducts(): List<Product>? {
        val allProductList = AppRoomDatabase
                .getInstance(this@MainActivity)
                ?.getProductDao()
                ?.getAllProduct()
        return allProductList
    }


    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }


}



