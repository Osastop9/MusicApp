package com.example.musicapptest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapptest.model.Music
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class SelectMusicFragment : Fragment() {
    private lateinit var music: Music
    private lateinit var rvPlaylist: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_select_music, container, false)

        val jsonString = getJsonPlaylist(requireContext())

        val gson = Gson()
        val listMusicType = object : TypeToken<Music>() {}.type
        music = gson.fromJson(jsonString, listMusicType)
//        music.forEachIndexed { idx, item -> Log.i("data", "> Item $idx:\n${item.source}") }

        val adapter = MusicAdapter(requireContext(), music)
        rvPlaylist = root.findViewById(R.id.rvPlaylist)
        rvPlaylist.adapter = adapter
        rvPlaylist.layoutManager = LinearLayoutManager(requireContext())

        return root
    }

//    override fun onResume() {
//        super.onResume()
//
//
//    }

    private fun getJsonPlaylist(context: Context, fileName: String = "playlist.json"): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
