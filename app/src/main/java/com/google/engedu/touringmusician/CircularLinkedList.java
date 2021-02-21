/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {
    Node tail;
    Node head;
    private class Node {
        Point point;
        Node prev, next;
        Node (Point point, Node prev, Node next) {
            this.point = point;
            this.prev = prev;
            this.next = next;
        }
    }



    public void insertBeginning(Point p) {
        if (!iterator().hasNext()) {
            insertFirstNode(p);
        }
        else {
            Log.d("insertBeginning", "Point added");
            Node prevHead = head;
            head = new Node(p, prevHead.prev, prevHead);
            prevHead.prev = head;
            Node curr = prevHead;
            while (curr.next != prevHead) {
                curr = curr.next;
            }
            tail = curr;
            curr.next = head;
        }

    }

    public void insertEnd(Point p) {
        Node newNode = new Node(p, tail, head);
        tail.next = newNode;
        head.prev = newNode;
        tail = newNode;
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        Log.d("totalDistance", "distanceUpdated");
        Point from = null;
        for (Iterator<Point> iter = iterator(); iter.hasNext();) {
            Point p = iter.next();
            if (from != null) {
                total += distanceBetween(from, p);
            }
            from = p;
        }
        return total;
    }

    public void insertNearest(Point p) {
        if (!iterator().hasNext()) {
            insertFirstNode(p);
        }
        else {
            // set nearest equal to distance between point and head point then check if
            // other points are closer
            float nearest = distanceBetween(p, head.point);
            Node nearestNode = head;
            Node curr = head.next;
            while (curr != head) {
                Point point2 = curr.point;
                if (distanceBetween(p, point2) < nearest) {
                    nearest = distanceBetween(p, point2);
                    nearestNode = curr;
                }
                curr = curr.next;
            }
            // insert Point p after nearestNode
            Node nextNode = nearestNode.next;
            Node newNode = new Node(p, nearestNode, nextNode);
            nearestNode.next = newNode;
            nextNode.prev = nearestNode;
        }
    }

    public void insertSmallest(Point p) {
        if (!iterator().hasNext()) {
            insertFirstNode(p);
        }
        else if (head.next == head) {
            // if there is only 1 node in the list, insert the new node at the beginning of list
            insertBeginning(p);
        }
        else {
            // set smallest distance change equal to inserting node at front of list
            float smallestDistanceChange = distanceBetween(p, head.point);
            Node insertionNode = head;
            Node insertionNode2 = null;
            // check if adding at end of tail gives a shorter distance
            if (distanceBetween(p, tail.point) < smallestDistanceChange) {
                smallestDistanceChange = distanceBetween(p, tail.point);
                insertionNode = tail;
            }
            Node ptr = head;
            Node ptr2 = head.next;
            while (ptr2 != head) {
                float distanceChange = distanceBetween(p, ptr.point) +
                        distanceBetween(p, ptr2.point) - distanceBetween(ptr.point, ptr2.point);
                if (distanceChange < smallestDistanceChange) {
                    smallestDistanceChange = distanceChange;
                    insertionNode = ptr;
                    insertionNode2 = ptr2;
                }
                ptr = ptr.next;
                ptr2 = ptr2.next;
            }
            if ((insertionNode == head) && (insertionNode2 == null)) {
                insertBeginning(p);
            }
            else if ((insertionNode == tail) && (insertionNode2 == null)) {
                insertEnd(p);
            }
            else {
                // else insert between the two pointer nodes
                Node newNode = new Node(p, insertionNode, insertionNode2);
                insertionNode.next = newNode;
                insertionNode2.prev = newNode;
            }
        }
    }

    private void insertFirstNode(Point p) {
        Log.d("insertBeginning", "First point added");
        head = new Node(p, null, null);
        head.prev = head;
        head.next = head;
        tail = head;
    }

    public void reset() {
        head = null;
        tail = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
