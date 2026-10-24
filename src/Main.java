import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Expense> expenses = new ArrayList<>();

        JFrame frame = new JFrame("Jledger");
        frame.setSize(600, 450);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField amountField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField paymentField = new JTextField();
        JButton addButton = new JButton("Add");

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Amount"));
        form.add(amountField);
        form.add(new JLabel("Category"));
        form.add(categoryField);
        form.add(new JLabel("Location"));
        form.add(locationField);
        form.add(new JLabel("Payment mode"));
        form.add(paymentField);
        form.add(new JLabel(""));
        form.add(addButton);
        frame.add(form, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Amount", "Category", "Location", "Payment"}, 0);
        JTable table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JLabel totalLabel = new JLabel("Total: 0.00");
        java.io.File file = new java.io.File("expenses.csv");
if (file.exists()) {
    try (java.util.Scanner sc = new java.util.Scanner(file)) {
        while (sc.hasNextLine()) {
            String[] p = sc.nextLine().split(",", -1);
            Expense e = new Expense();
            e.id = Integer.parseInt(p[0]);
            e.amount = Double.parseDouble(p[1]);
            e.category = p[2];
            e.location = p[3];
            e.paymentMode = p[4];
            expenses.add(e);
            model.addRow(new Object[]{e.id, e.amount, e.category, e.location, e.paymentMode});
        }
    } catch (Exception ex) {
        System.out.println("Could not load: " + ex.getMessage());
    }
    double sum = 0;
    for (Expense x : expenses) {
        sum += x.amount;
    }
    totalLabel.setText("Total: " + String.format("%.2f", sum));
}
        frame.add(totalLabel, BorderLayout.SOUTH);

        addButton.addActionListener(ev -> {
            try {
                Expense e = new Expense();
                e.id = expenses.size() + 1;
                e.amount = Double.parseDouble(amountField.getText());
                e.category = categoryField.getText();
                e.location = locationField.getText();
                e.paymentMode = paymentField.getText();
                expenses.add(e);
                try (java.io.FileWriter fw = new java.io.FileWriter("expenses.csv", true)) {
    fw.write(e.id + "," + e.amount + ","
        + e.category.replace(",", " ") + ","
        + e.location.replace(",", " ") + ","
        + e.paymentMode.replace(",", " ") + "\n");
} catch (java.io.IOException ex) {
    System.out.println("Could not save");
}
                model.addRow(new Object[]{e.id, e.amount, e.category, e.location, e.paymentMode});

                double total = 0;
                for (Expense x : expenses) {
                    total += x.amount;
                }
                totalLabel.setText("Total: " + String.format("%.2f", total));

                amountField.setText("");
                categoryField.setText("");
                locationField.setText("");
                paymentField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Amount must be a number");
            }
        });

        frame.setVisible(true);
    }
}
