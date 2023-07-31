import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RectangleDrawer extends JFrame {
    private JPanel drawingPanel;
    private List<Rectangle> rectangles;
    private Rectangle currentRectangle;
    private boolean isDragging;

    public RectangleDrawer() {
        setTitle("Rectangle Drawer");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rectangles = new ArrayList<>();
        isDragging = false;

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Rectangle rectangle : rectangles) {
                    g.setColor(Color.BLUE);
                    g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
            }
        };

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    // Create a new rectangle on single-click
                    createRectangle(e.getX(), e.getY());
                } else if (e.getClickCount() == 2) {
                    // Delete the rectangle on double-click
                    deleteRectangle(e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Check if the mouse is pressed inside a rectangle
                for (Rectangle rectangle : rectangles) {
                    if (rectangle.contains(e.getX(), e.getY())) {
                        isDragging = true;
                        currentRectangle = rectangle;
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Reset dragging state when the mouse is released
                isDragging = false;
                currentRectangle = null;
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Change the cursor shape when hovering over a rectangle or blank space
                boolean overRectangle = false;
                for (Rectangle rectangle : rectangles) {
                    if (rectangle.contains(e.getX(), e.getY())) {
                        overRectangle = true;
                        break;
                    }
                }
                drawingPanel.setCursor(overRectangle ? Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR)
                        : Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Drag the current rectangle when the mouse is dragged
                if (isDragging && currentRectangle != null) {
                    currentRectangle.setLocation(e.getX(), e.getY());
                    drawingPanel.repaint();
                }
            }
        });

        add(drawingPanel);
        setVisible(true);
    }

    private void createRectangle(int x, int y) {
        currentRectangle = new Rectangle(x, y, 50, 30);
        rectangles.add(currentRectangle);
        drawingPanel.repaint();
    }

    private void deleteRectangle(int x, int y) {
        Iterator<Rectangle> iterator = rectangles.iterator();
        while (iterator.hasNext()) {
            Rectangle rectangle = iterator.next();
            if (rectangle.contains(x, y)) {
                iterator.remove();
                drawingPanel.repaint();
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RectangleDrawer());
    }
}
