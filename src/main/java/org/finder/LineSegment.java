package org.finder;

import java.awt.*;

class LineSegment {
    double x1, y1, x2, y2;
    double length;
    boolean visited;
    int lineNumberId;
    boolean isPolyline;
    int connectedSegmentNextId;
    int connectedSegmentPreviousId;
    Color lineColor;

    public LineSegment(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        this.visited = false;
    }

    public double getLength() {
        return length;
    }

    public boolean isConnected(LineSegment other) {
        return this.x2 == other.x1 && this.y2 == other.y1 ||
                this.x2 == other.x2 && this.y2 == other.y2 ||
                this.x1 == other.x1 && this.y1 == other.y1 ||
                this.x1 == other.x2 && this.y1 == other.y2;
    }



    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }


    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public boolean isPolyline() {
        return isPolyline;
    }

    public void setPolyline(boolean polyline) {
        isPolyline = polyline;
    }

    public int getLineNumberId() {
        return lineNumberId;
    }

    public void setLineNumberId(int lineNumberId) {
        this.lineNumberId = lineNumberId;
    }

    public int getConnectedSegmentNextId() {
        return connectedSegmentNextId;
    }

    public void setConnectedSegmentNextId(int connectedSegmentNextId) {
        this.connectedSegmentNextId = connectedSegmentNextId;
    }

    public int getConnectedSegmentPreviousId() {
        return connectedSegmentPreviousId;
    }

    public void setConnectedSegmentPreviousId(int connectedSegmentPreviousId) {
        this.connectedSegmentPreviousId = connectedSegmentPreviousId;
    }

    @Override
    public String toString() {
        return "LineSegment{" +
                "lineNumberId=" + lineNumberId +
                ", x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", length=" + length +
                ", visited=" + visited +
                ", isPolyline=" + isPolyline +
                ", connectedSegmentNextId=" + connectedSegmentNextId +
                ", connectedSegmentPreviousId=" + connectedSegmentPreviousId +
                '}';
    }
}
