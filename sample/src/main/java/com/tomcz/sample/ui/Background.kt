package com.tomcz.sample.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill

@Composable
fun BezierBackground() {
    DrawBlueBlob()
    DrawGrayBlob()
    DrawOrangeBlob()
}

@Composable
private fun DrawOrangeBlob() {
    Canvas(modifier = Modifier.fillMaxSize()) {

        val path = Path()

        val startX = 0f
        val startY = size.height * .17f

        val end1X = size.width * .4f
        val end1Y = size.height * .06f

        val end2X = size.width * .75f
        val end2Y = 0f

        val anc0X = size.width * .15f
        val anc0Y = size.height * .18f

        val anc1X = size.width * .1f
        val anc1Y = size.height * .04f

        val anc2X = size.width * .6f
        val anc2Y = size.height * .07f

        val anc3X = size.width * 0.6f
        val anc3Y = 0f

        path.moveTo(startX, startY)
        path.cubicTo(anc0X, anc0Y, anc1X, anc1Y, end1X, end1Y)
        path.cubicTo(anc2X, anc2Y, anc3X, anc3Y, end2X, end2Y)
        path.lineTo(0f, 0f)
        path.close()

        drawPath(
            path = path,
            color = Orange,
            style = Fill
        )
    }
}

@Composable
private fun DrawGrayBlob() {
    Canvas(modifier = Modifier.fillMaxSize()) {

        val path = Path()

        val startX = 0f
        val startY = size.height * .4f

        val end1X = size.width * .6f
        val end1Y = size.height * .3f

        val end2X = size.width
        val end2Y = size.height * .12f

        val anc0X = size.width * .5f
        val anc0Y = size.height * .5f

        val anc2X = size.width * .68f
        val anc2Y = size.height * .14f

        val anc3X = size.width * 0.9f
        val anc3Y = size.height * .2f

        path.moveTo(startX, startY)
        path.quadraticBezierTo(anc0X, anc0Y, end1X, end1Y)
        path.cubicTo(anc2X, anc2Y, anc3X, anc3Y, end2X, end2Y)
        path.lineTo(size.width, 0f)
        path.lineTo(0f, 0f)
        path.close()

        drawPath(
            path = path,
            color = DarkGray,
            style = Fill
        )
    }
}

@Composable
private fun DrawBlueBlob() {
    Canvas(modifier = Modifier.fillMaxSize()) {

        val path = Path()

        val startX = size.width * .4f
        val startY = size.height * .4f

        val endX = size.width
        val endY = size.height * .43f

        val ancX = size.width * .86f
        val ancY = size.height * .5f

        path.moveTo(startX, startY)
        path.quadraticBezierTo(ancX, ancY, endX, endY)
        path.lineTo(size.width, 0f)
        path.lineTo(startX, 0f)
        path.close()

        drawPath(
            path = path,
            color = Teal,
            style = Fill
        )
    }
}
