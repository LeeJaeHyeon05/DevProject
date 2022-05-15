package fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.devproject.addConferences.AddConferencesActivity
import com.example.devproject.databinding.FragmentHomeBinding
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler

class HomeFragment : Fragment() {
    private var mBinding : FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val swipeRefreshLayout : SwipeRefreshLayout? = mBinding?.swiperRefreshLayout

        swipeRefreshLayout?.setOnRefreshListener {
            DataHandler.reload()
            swipeRefreshLayout.isRefreshing = false
        }

        mBinding = binding
        UIHandler.allocateUI(mBinding?.root!!.rootView)
        UIHandler.activateUI(mBinding?.conferRecyclerView!!.id)
        addConferences()
        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    private fun addConferences() {
        val addCon = mBinding?.conferAddButton
        addCon?.setOnClickListener {
            if(FirebaseIO.isValidAccount()){
                val intent = Intent(activity, AddConferencesActivity::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(this.context, "로그인이 필요합니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}