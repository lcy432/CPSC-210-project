package ui;

import javax.swing.*;
import java.awt.*;

public class StatsBarChartPanel extends JPanel {
    private final double actualRate;
    private final double officialRate;

    public StatsBarChartPanel(double actualRate, double officialRate) {
        this.actualRate = actualRate;
        this.officialRate = officialRate;
        setPreferredSize(new Dimension(400, 300));
    }
    
    // REQUIRES: g is not null
    // MODIFIES: this
    // EFFECTS: Draws a bar chart comparing actual and official desired 5-star rates onto the panel.
    //          Actual rate is represented by a blue bar, and official rate by an orange bar.
    //          (绘制实际与官方五星概率对比的柱状图到面板上，蓝色为实际概率，橙色为官方概率)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        int barWidth = width / 5;
        int maxBarHeight = height - 100;

        int actualBarHeight = (int)(actualRate * maxBarHeight);
        int officialBarHeight = (int)(officialRate * maxBarHeight);

        // Draw actual rate bar (blue)
        g2.setColor(Color.BLUE);
        int actualX = width / 3 - barWidth / 2;
        g2.fillRect(actualX, height - actualBarHeight - 50, barWidth, actualBarHeight);
        g2.setColor(Color.BLACK);
        g2.drawString(String.format("Actual %.1f%%", actualRate * 100), actualX - 10, height - 30);

        // Draw official rate bar (orange)
        g2.setColor(Color.ORANGE);
        int officialX = 2 * width / 3 - barWidth / 2;
        g2.fillRect(officialX, height - officialBarHeight - 50, barWidth, officialBarHeight);
        g2.setColor(Color.BLACK);
        g2.drawString(String.format("Official %.1f%%", officialRate * 100), officialX - 15, height - 30);

        // Draw axes
        g2.drawLine(30, height - 50, width - 30, height - 50); // x-axis
        g2.drawLine(30, height - 50, 30, 30);                  // y-axis
    }
}