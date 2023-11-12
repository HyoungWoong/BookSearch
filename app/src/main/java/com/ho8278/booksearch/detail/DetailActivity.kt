package com.ho8278.booksearch.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.ho8278.booksearch.databinding.ActivityDetailBinding
import com.ho8278.data.repository.model.BooksResult
import com.ho8278.lib.lifecycle.activity.LifecycleActivity
import com.ho8278.lib.lifecycle.activity.untilLifecycle
import com.ho8278.lib.serialize.Serializer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : LifecycleActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DetailViewModel>()

    @Inject
    lateinit var serializer: Serializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent.hasExtra(KEY_BOOK_RESULT)) {
            val booksResult = serializer.deserialize<BooksResult>(
                intent.getStringExtra(KEY_BOOK_RESULT)!!,
                BooksResult::class.java
            )
            println(booksResult)
            viewModel.booksResult.tryEmit(booksResult)
        }

        viewModel.booksResult
            .onEach {
                binding.title.text = it?.title.orEmpty()
                binding.subtitle.text = it?.subtitle.orEmpty()
                binding.author.text = it?.authors.orEmpty()
                binding.publisher.text = it?.publisher.orEmpty()
                binding.language.text = it?.language.orEmpty()
                binding.isbn10.text = it?.isbn10.orEmpty()
                binding.isbn13.text = it?.isbn13.orEmpty()
                binding.page.text = it?.pages.orEmpty()
                binding.year.text = it?.year.orEmpty()
                binding.rating.text = it?.rating.orEmpty()
                binding.description.text = it?.desc.orEmpty()
                binding.price.text = it?.price.orEmpty()

                binding.pdfList.removeAllViews()
                it?.pdf?.entries?.forEach { (text, link) ->
                    val textView = createLinkText(link, text)
                    binding.pdfList.addView(textView)
                }

                Glide.with(this)
                    .load(it?.image)
                    .into(binding.image)
            }
            .untilLifecycle(this)
    }

    private fun createLinkText(link: String, text: String): TextView {
        return TextView(baseContext).apply {
            val spannableString = SpannableStringBuilder(text).apply {
                setSpan(UnderlineSpan(), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            this.text = spannableString
            setOnClickListener { executeDeeplink(link) }
        }
    }

    private fun executeDeeplink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    companion object {
        const val KEY_BOOK_RESULT = "key_book_result"
    }
}