package ui;


import model.GachaHistory;
import model.GachaPull;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import model.Event;
import model.EventLog;




public class GachaTrackerGUI extends JFrame {
    private static final String DATA_FILE = "./data/gachaHistory.json";

    private GachaHistory history;
    private JsonReader reader;
    private JsonWriter writer;
    private DefaultListModel<String> listModel;

    private JList<String> recordList; // 新增定义

    public GachaTrackerGUI() {
        super("Gacha Statistics Tracker"); // 抽卡统计记录器
        history = new GachaHistory();
        reader = new JsonReader(DATA_FILE);
        writer = new JsonWriter(DATA_FILE);

        initializeUI();
    }

    // MODIFIES: this
    // EFFECTS: Initializes and sets up the basic GUI layout (初始化并设置基本界面布局)
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null); 
    
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        JLabel titleLabel = new JLabel("Welcome to Gacha Statistics Tracker", JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH); // 抽取按钮面板创建
    
        listModel = new DefaultListModel<>();
        recordList = new JList<>(listModel);
        mainPanel.add(new JScrollPane(recordList), BorderLayout.CENTER);

        add(mainPanel);

        // 添加窗口关闭监听器输出日志事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Event log since app started:");
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.toString());
                }
            }
        });
        setVisible(true);
    }
    
    // 仅抽取按钮创建逻辑
    // EFFECTS: Create Button Layout
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
    
        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(e -> loadGachaHistory());
    
        JButton saveButton = new JButton("Save Data");
        saveButton.addActionListener(e -> saveGachaHistory());
    
        JButton addButton = new JButton("Add New Pull");
        addButton.addActionListener(e -> addNewGachaPull());
    
        JButton deleteButton = new JButton("Delete Selected Pull");
        deleteButton.addActionListener(e -> deleteSelectedPull(recordList));
    
        JButton statsButton = new JButton("Stats");
        statsButton.addActionListener(e -> showStatsDialog());
    
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(statsButton);
    
        return buttonPanel;
    }

    // MODIFIES: this, history
    // EFFECTS: Loads gacha history from JSON file and shows confirmation dialog
    private void loadGachaHistory() {
        try {
            history = reader.read();
            listModel.clear(); // Clear previous records 清除旧记录
            for (GachaPull pull : history.getAllPulls()) {
                listModel.addElement("Pull #" + pull.getPullIndex() // 第x次抽卡
                        + ": Desired 5-star? " + pull.isDesired5Star() // 是否期望的五星
                        + ", #4-stars: " + pull.getNumberOf4Stars() // 四星数量
                        + ", Total draws: " + pull.getDrawCount()); // 总抽数
            }
            JOptionPane.showMessageDialog(this, "Data loaded successfully!"); // 数据加载成功！
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load data: " + ex.getMessage()); // 数据加载失败
        }
    }

    // MODIFIES: none
    // EFFECTS: Saves the current gacha history data to a JSON file and shows a
    // confirmation dialog (将当前的抽卡历史数据保存到 JSON 文件，并显示成功或失败提示)
    private void saveGachaHistory() {
        try {
            writer.open();
            writer.write(history);
            writer.close();
            JOptionPane.showMessageDialog(this, "Data saved successfully!"); // 数据保存成功！
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save data: " + ex.getMessage()); // 数据保存失败
        }
    }
    // REQUIRES: none
    // MODIFIES: this, history, listModel
    // EFFECTS: Prompts user to input details for a new gacha pull and adds it to the history.
    //          If the user cancels input, does nothing. Displays confirmation or error dialogs accordingly.
    //          (弹窗提示用户输入新的抽卡记录信息，并添加到 history 中；如果用户取消输入则不做任何修改；并显示相应的提示信息)

    private void addNewGachaPull() {
        try {
            int pullIndex = promptForInt("Enter pull index (integer):");
            boolean desired5Star = promptForYesNo("Was it the desired 5-star? (yes/no):");
            int numberOf4Stars = promptForInt("Enter number of 4-star items:");
            int drawCount = promptForInt("Enter total draw count:");

            addPullAndUpdateView(pullIndex, desired5Star, numberOf4Stars, drawCount);

            JOptionPane.showMessageDialog(this, "Record added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input format error! Please enter valid numbers.");
        } catch (RuntimeException ex) {
            // 用户取消输入时静默退出，无需额外提示
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage());
        }
    }

    // REQUIRES: message is not null
    // MODIFIES: none
    // EFFECTS: Prompts user with an input dialog displaying the given message and returns the entered integer.
    //          If user cancels input, throws a RuntimeException.
    //          (显示输入框，返回用户输入的整数；若用户取消则抛出RuntimeException)
    private int promptForInt(String message) {
        String input = JOptionPane.showInputDialog(this, message);
        if (input == null) {
            throw new RuntimeException("Input cancelled by user.");
        }
        return Integer.parseInt(input.trim());
    }

    // REQUIRES: message is not null
    // MODIFIES: none
    // EFFECTS: Prompts user with an input dialog displaying the given message
    // and returns true if user enters "yes" (ignoring case).
    //          Returns false otherwise. If user cancels input, throws a RuntimeException.
    //          (显示输入框，返回用户输入的yes/no布尔值；若用户取消则抛出RuntimeException)
    private boolean promptForYesNo(String message) {
        String input = JOptionPane.showInputDialog(this, message);
        if (input == null) {
            throw new RuntimeException("Input cancelled by user.");
        }
        return input.trim().equalsIgnoreCase("yes");
    }
    
    // REQUIRES: numberOf4Stars >= 0, drawCount >= 0
    // MODIFIES: this, history, listModel
    // EFFECTS: Creates a new GachaPull object using given details, adds it to the history,
    //          and updates the GUI listModel to display the new record.
    //          (创建新的GachaPull记录，更新历史记录和GUI界面显示)
    private void addPullAndUpdateView(int pullIndex, boolean desired5Star, int numberOf4Stars, int drawCount) {
        GachaPull newPull = new GachaPull(desired5Star, numberOf4Stars, pullIndex, drawCount);
        history.addPull(newPull);
        listModel.addElement("Pull #" + pullIndex + ": Desired 5-star? " + desired5Star
                + ", #4-stars: " + numberOf4Stars + ", Total draws: " + drawCount);
    }

    // REQUIRES: recordList != null
    // MODIFIES: this, history, listModel
    // EFFECTS: Deletes selected pull from history and updates the GUI immediately.
    // If no pull is selected, shows an error dialog.
    private void deleteSelectedPull(JList<String> recordList) {
        int selectedIndex = recordList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a pull record to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Remove from data model (GachaHistory)
        history.removePull(selectedIndex);

        // Remove from GUI list model to update interface immediately
        listModel.remove(selectedIndex);

        JOptionPane.showMessageDialog(this,
                "Selected pull record deleted successfully!");
    }

    // REQUIRES: none
    // MODIFIES: none
    // EFFECTS: Displays a dialog window showing current statistics including 
    // desired 5-star rate and average number of 4-star items per pull. 
    //          Allows the user to input an official desired 5-star rate for comparison. 
    //          If user clicks OK, performs threshold comparison.
    //          (显示统计数据弹窗界面，包括五星概率、四星平均个数，并允许输入官方概率进行比较；用户点击确定后进行概率比较)
    private void showStatsDialog() {
        // 计算统计数据
        double desiredRate = history.calculateFiveStarRate();
        double fourStarAvg = history.calculateFourStarAvg();

        // 创建弹窗界面
        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        // 显示当前统计数据
        statsPanel.add(new JLabel("Desired 5-star rate: " + String.format("%.2f%%", desiredRate * 100)));
        statsPanel.add(new JLabel("Average number of 4-star items per pull: " + String.format("%.2f", fourStarAvg)));

        // 输入官方五星概率
        JTextField thresholdField = new JTextField();
        statsPanel.add(new JLabel("Enter official desired 5-star rate (e.g., 0.60):"));
        statsPanel.add(thresholdField);

        // 显示确认和取消按钮
        int result = JOptionPane.showConfirmDialog(this, statsPanel,
                "Gacha Stats", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        // 用户点击确定时进行概率比较
        if (result == JOptionPane.OK_OPTION) {
            performThresholdComparison(thresholdField.getText(), desiredRate);
        }
    }

    // REQUIRES: thresholdStr is a valid string representing a decimal number between 0 and 1
    // MODIFIES: none
    // EFFECTS: Parses user's input threshold and compares it with the actual desired 5-star rate. 
    //          Displays a dialog informing user whether actual rate is above or below the official threshold.
    //          Then opens a new window displaying a bar chart comparison. If input is invalid, shows error dialog.
    //          (根据输入的官方概率与实际概率进行比较，并弹窗提示用户比较结果，然后打开柱状图窗口；若输入无效则显示错误提示)
    private void performThresholdComparison(String thresholdStr, double actualRate) {
        try {
            double threshold = Double.parseDouble(thresholdStr.trim());

            String message;
            if (history.isAboveThreshold(threshold)) {
                message = String.format(
                        "Your actual desired 5-star rate (%.2f%%) is ABOVE the official threshold (%.2f%%).",
                        actualRate * 100, threshold * 100);
            } else {
                message = String.format(
                        "Your actual desired 5-star rate (%.2f%%) is BELOW the official threshold (%.2f%%).",
                        actualRate * 100, threshold * 100);
            }

            JOptionPane.showMessageDialog(this, message, "Comparison Result", JOptionPane.INFORMATION_MESSAGE);

            displayChart(actualRate, threshold); // 仅提取图表方法即可
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid threshold input. Please enter a valid number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // REQUIRES: 0.0 <= actualRate, threshold <= 1.0
    // MODIFIES: none
    // EFFECTS: Opens a new window displaying a comparison bar chart between actual and official desired 5-star rates.
    //          (打开新窗口，显示实际与官方五星概率对比的柱状图)
    private void displayChart(double actualRate, double threshold) {
        JFrame chartFrame = new JFrame("5-Star Rate Comparison");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.add(new StatsBarChartPanel(actualRate, threshold));
        chartFrame.pack();
        chartFrame.setLocationRelativeTo(this);
        chartFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new GachaTrackerGUI();
    }
}
