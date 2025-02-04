package com.example.myapplication.util

import com.example.myapplication.data.model.SeaCreature

class Rectangle(val x: Float, val y: Float, val width: Float, val height: Float) {
    fun contains(point: SeaCreature): Boolean {
        return point.position.first >= x && point.position.first <= x + width &&
                point.position.second >= y && point.position.second <= y + height
    }

    fun intersects(range: Rectangle): Boolean {
        return !(range.x > x + width || range.x + range.width < x ||
                range.y > y + height || range.y + range.height < y)
    }
}

class QuadTree(private val boundary: Rectangle, private val capacity: Int = 4) {
    private val points = mutableListOf<SeaCreature>()
    private var divided = false
    private var topLeft: QuadTree? = null
    private var topRight: QuadTree? = null
    private var bottomLeft: QuadTree? = null
    private var bottomRight: QuadTree? = null

    private fun subdivide() {
        val x = boundary.x
        val y = boundary.y
        val w = boundary.width / 2
        val h = boundary.height / 2

        topLeft = QuadTree(Rectangle(x, y, w, h), capacity)
        topRight = QuadTree(Rectangle(x + w, y, w, h), capacity)
        bottomLeft = QuadTree(Rectangle(x, y + h, w, h), capacity)
        bottomRight = QuadTree(Rectangle(x + w, y + h, w, h), capacity)

        divided = true
    }

    fun insert(point: SeaCreature): Boolean {
        if (!boundary.contains(point)) return false

        if (points.size < capacity) {
            points.add(point)
            return true
        }

        if (!divided) subdivide()

        return topLeft!!.insert(point) || topRight!!.insert(point) ||
                bottomLeft!!.insert(point) || bottomRight!!.insert(point)
    }

    fun query(range: Rectangle, found: MutableList<SeaCreature>) {
        if (!boundary.intersects(range)) return

        for (point in points) {
            if (range.contains(point)) {
                found.add(point)
            }
        }

        if (!divided) return

        topLeft!!.query(range, found)
        topRight!!.query(range, found)
        bottomLeft!!.query(range, found)
        bottomRight!!.query(range, found)
    }
}