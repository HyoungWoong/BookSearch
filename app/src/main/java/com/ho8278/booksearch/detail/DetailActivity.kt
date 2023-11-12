package com.ho8278.booksearch.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.ho8278.booksearch.R
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

        loadStartArgs()
        bindData()
    }

    private fun loadStartArgs() {
        if (intent.hasExtra(KEY_BOOK_RESULT)) {
            val booksResult = serializer.deserialize<BooksResult>(
                intent.getStringExtra(KEY_BOOK_RESULT)!!,
                BooksResult::class.java
            )
            viewModel.booksResult.tryEmit(booksResult)
        }
    }

    private fun bindData() {
        viewModel.booksResult
            .onEach { result ->
                binding.title.text = result?.title.orEmpty()
                binding.subtitle.text = result?.subtitle.orEmpty()
                binding.author.text = getString(R.string.authors, result?.authors.orEmpty())
                binding.publisher.text = getString(R.string.publisher, result?.publisher.orEmpty())
                binding.language.text = result?.language.orEmpty()
                binding.isbn10.text = getString(R.string.isbn, result?.isbn10.orEmpty())
                binding.isbn13.text = getString(R.string.isbn, result?.isbn13.orEmpty())
                binding.page.text = getString(R.string.pages, result?.pages.orEmpty())
                binding.year.text = getString(R.string.year, result?.year.orEmpty())
                binding.rating.text = getString(R.string.rating, result?.rating.orEmpty())
                binding.description.text = result?.desc.orEmpty()
                binding.price.text = getString(R.string.price, result?.price.orEmpty())

                binding.goWeb.visibility =
                    if (result?.url.isNullOrEmpty()) View.GONE else View.VISIBLE
                binding.goWeb.setOnClickListener {
                    if (!result?.url.isNullOrEmpty()) {
                        executeDeeplink(result!!.url)
                    }
                }

                binding.pdfList.removeAllViews()
                result?.pdf?.entries?.forEach { (text, link) ->
                    val textView = createLinkText(link, text)
                    binding.pdfList.addView(textView)
                }

                Glide.with(this)
                    .load(result?.image)
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