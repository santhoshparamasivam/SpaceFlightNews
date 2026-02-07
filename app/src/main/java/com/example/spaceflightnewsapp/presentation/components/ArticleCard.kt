package com.example.spaceflightnewsapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.presentation.utils.DateUtils

@Composable
fun ArticleCard(article: Article, onClick: () -> Unit) {

    Card(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() }
    ) {
        Column {
            AsyncImage(
                model = article.image_url,
                contentDescription = null,
                modifier = Modifier.height(180.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(Modifier.padding(12.dp)) {
                Text(article.news_site, fontSize = 12.sp)
                Text(article.title, fontWeight = FontWeight.Bold)

                Text(
                    if (article.authors.isNotEmpty())
                        "${article.authors[0].name} • ${DateUtils.formatIsoDate(article.published_at)}"
                    else DateUtils.formatIsoDate(article.published_at),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
