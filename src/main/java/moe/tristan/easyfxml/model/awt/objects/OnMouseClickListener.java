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

package moe.tristan.easyfxml.model.awt.objects;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

public class OnMouseClickListener implements MouseListener {

    protected final Consumer<MouseEvent> onMouseClicked;

    public OnMouseClickListener(final Consumer<MouseEvent> onMouseClicked) {
        this.onMouseClicked = onMouseClicked;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        onMouseClicked.accept(e);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        // ignore
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        // ignore
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        // ignore
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        // ignore
    }

}
