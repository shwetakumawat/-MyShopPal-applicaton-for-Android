package com.example.myshoppal.ui.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.databinding.FragmentProductsBinding
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.Product
import com.example.myshoppal.ui.activities.AddProductActivity
import com.example.myshoppal.ui.activities.SettingsActivity
import com.example.myshoppal.ui.adapters.MyProductsListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    //private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    fun successProductListFromFirestore(productsList: ArrayList<Product>) {
        hideProgressDialog()

        if (productsList.size > 0) {
            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE

            rv_my_product_items.layoutManager = LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)

            val adapterProducts =
                MyProductsListAdapter(requireActivity(), productsList, this@ProductsFragment)

            rv_my_product_items.adapter = adapterProducts
        } else {
            rv_my_product_items.visibility = View.GONE
            tv_no_products_found.visibility = View.VISIBLE
        }

    }

    fun productDeleteSuccess(){
        hideProgressDialog()

        Toast.makeText(requireActivity(), getString(R.string.product_delete_success), Toast.LENGTH_SHORT).show()

        getProductListFromFireStore()
    }

    private fun getProductListFromFireStore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this@ProductsFragment)
    }

    override fun onResume() {
        super.onResume()

        getProductListFromFireStore()
    }

    fun deleteProduct(productID: String) {
       showAlertDialogToDeleteProduct(productID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialogToDeleteProduct(productID : String){
        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle(getString(R.string.delete_dialog_title))
        builder.setMessage(getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(getString(R.string.yes)){
                dialogInterface,_->

            showProgressDialog(getString(R.string.please_wait))

            FirestoreClass().deleteProduct(this,productID)

            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no)){
                dialogInterface,_->
            dialogInterface.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}