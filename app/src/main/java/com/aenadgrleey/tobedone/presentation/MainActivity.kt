package com.aenadgrleey.tobedone.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.aenadgrleey.tobedone.presentation.catalogue.CatalogueFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FragmentContainerView(this).apply {
            id = androidx.fragment.R.id.fragment_container_view_tag
        })
        if (supportFragmentManager.backStackEntryCount == 0)
            supportFragmentManager.commit {
                add<CatalogueFragment>(
                    androidx.fragment.R.id.fragment_container_view_tag,
                    "tag"
                )
                setReorderingAllowed(true)
            }

    }

}