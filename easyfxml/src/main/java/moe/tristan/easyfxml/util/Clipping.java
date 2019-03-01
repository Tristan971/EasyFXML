/*
 *     Lyrebird, a free open-source cross-platform twitter client.
 *     Copyright (C) 2017-2018, Tristan Deloche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package moe.tristan.easyfxml.util;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This is a helper class for a few clipping helper methods to make rounded corners rectangles or circles.
 */
public final class Clipping {

    private Clipping() {
    }

    /**
     * Builds a clip-ready rounded corners {@link Rectangle} that is square.
     *
     * @param side          The size of the size of this square
     * @param roundedRadius The radius of this square's corners rounding
     *
     * @return A square with the given side size and rounded corners with the given radius
     */
    public static Rectangle getSquareClip(final double side, final double roundedRadius) {
        final Rectangle rectangle = new Rectangle();
        rectangle.setHeight(side);
        rectangle.setWidth(side);
        rectangle.setArcWidth(roundedRadius);
        rectangle.setArcHeight(roundedRadius);
        return rectangle;
    }

    /**
     * Builds a clip-ready {@link Circle} with a specific radius and specific center position.
     *
     * @param radius  The radius of this circle.
     * @param centerX The horizontal position for this circle's center
     * @param centerY The vertical position for this circle's center
     *
     * @return A circle with the given radius and center position
     */
    private static Circle getCircleClip(final double radius, final double centerX, final double centerY) {
        final Circle clip = new Circle(radius);
        clip.setCenterX(centerX);
        clip.setCenterY(centerY);
        return clip;
    }

    /**
     * Builds a clip-ready {@link Circle} with a specific radius and its center moved so that it is moved to the
     * top-left of its container node.
     *
     * @param radius The radius of the circle in question
     *
     * @return A circle with the given radius
     */
    public static Circle getCircleClip(final double radius) {
        return getCircleClip(radius, radius, radius);
    }

}
