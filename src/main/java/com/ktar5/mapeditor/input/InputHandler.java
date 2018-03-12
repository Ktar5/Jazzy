package com.ktar5.mapeditor.input;

import java.awt.event.*;

public class InputHandler implements MouseListener, MouseMotionListener, ActionListener, KeyListener {









    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * If a mouse button is clicked.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        lastClickedTileX = e.getX() / Constants.TILE_WIDTH;
        lastClickedTileY = e.getY() / Constants.TILE_HEIGHT;
        if (ifLeftMouseButtonPressed(e)) {
            updateTile(lastClickedTileX, lastClickedTileY);
        }
    }

    private boolean ifLeftMouseButtonPressed(MouseEvent e) {
        return (e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK;
    }

    private boolean ifRightMouseButtonPressed(MouseEvent e) {
        return (e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK;
    }

    private void updateTile(int xCor, int yCor) {
        xCor = Math.max(0, Math.min(xCor, Constants.GRID_WIDTH - 1));
        yCor = Math.max(0, Math.min(yCor, Constants.GRID_HEIGHT - 1));
        if (guiInformation.getSelectedTile() != null) {
            camera.setTile(xCor, yCor, guiInformation.getSelectedTile().getCharacter());
        }
    }

    private void updateCamera(int newTileX, int newTileY) {
        if (newTileX != lastClickedTileX) {
            if (newTileX - lastClickedTileX > 0) {
                camera.moveCamera(GridCamera.WEST);
            } else {
                camera.moveCamera(GridCamera.EAST);
            }
        }
        if (newTileY != lastClickedTileY) {
            if (newTileY - lastClickedTileY > 0) {
                camera.moveCamera(GridCamera.NORTH);
            } else {
                camera.moveCamera(GridCamera.SOUTH);
            }
        }
        lastClickedTileX = newTileX;
        lastClickedTileY = newTileY;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * If the user keeps the mouse button pressed it will keep drawing if it is
     * in drawing mode, which it is by default.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (ifRightMouseButtonPressed(e)) {
            int newTileX = e.getX() / Constants.TILE_WIDTH;
            int newTileY = e.getY() / Constants.TILE_HEIGHT;
            updateCamera(newTileX, newTileY);
        }
        this.mousePressed(e);
    }

    /**
     * If the mouse cursor in moved.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * If a key is pressed down.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            camera.moveCamera(GridCamera.NORTH);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            camera.moveCamera(GridCamera.EAST);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            camera.moveCamera(GridCamera.SOUTH);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            camera.moveCamera(GridCamera.WEST);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}