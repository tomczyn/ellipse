package com.tomcz.ellipse

interface Partial<ST : Any> {
    fun reduce(oldState: ST): ST
}
