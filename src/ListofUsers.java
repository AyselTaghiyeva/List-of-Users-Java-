import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

public class ListofUsers {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtCountry;
    private JButton saveButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;
    private JTable table1;
    private JTextField txtSearch;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ListofUsers");
        frame.setContentPane(new ListofUsers().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;

    public void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/list_of_users","root","9911");
            System.out.println("Success!");
        }
        catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

    }


    //Table users
    void table_load(){
        try {
            //pst = con.prepareStatement("select * from users");
            pst = con.prepareStatement("SELECT * FROM list_of_users.users;");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }


    //Save
    public ListofUsers() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String fullName, email, country;

                fullName = txtName.getText();
                email = txtEmail.getText();
                country = txtCountry.getText();
                try {
                    //pst = con.prepareStatement("insert into users(fullName,email,country)values(?,?,?)");
                    pst = con.prepareStatement("INSERT INTO `list_of_users`.`users` (`fullName`, `email`, `country`) VALUES(?,?,?)");
                    pst.setString(1, fullName);
                    pst.setString(2, email);
                    pst.setString(3, country);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Added!");
                    table_load();
                    txtName.setText("");
                    txtEmail.setText("");
                    txtCountry.setText("");
                    txtName.requestFocus();
                }
                catch(SQLException e1)
                {
                    e1.printStackTrace();
                }

            }
        });

        //Search
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String userId = txtSearch.getText();

                    try {
                        //pst = con.prepareStatement("select fullName,email,country from users where Id=?");
                        pst = con.prepareStatement("SELECT fullName, email, country FROM list_of_users.users where Id=?;");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        pst.setString(1, userId);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    ResultSet rs = null;
                    try {
                        rs = pst.executeQuery();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        if(rs.next()==true){
                            String userfullName = rs.getString(1);
                            String useremail = rs.getString(2);
                            String usercountry = rs.getString(3);

                            txtName.setText(userfullName);
                            txtEmail.setText(useremail);
                            txtCountry.setText(usercountry);

                        }else{
                            txtName.setText("");
                            txtEmail.setText("");
                            txtCountry.setText("");
                            JOptionPane.showMessageDialog(null, "Invalid User Id");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (RuntimeException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });

        //Update
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userid, fullName, email, country;

                fullName = txtName.getText();
                email = txtEmail.getText();
                country = txtCountry.getText();
                userid = txtSearch.getText();


                try {
                    //pst = con.prepareStatement("update users set fullName = ?,email = ?,country = ? where Id = ?");
                    pst = con.prepareStatement("UPDATE `list_of_users`.`users` SET `fullName` = ?,`email` = ?,`country` = ? WHERE (`Id` = ?);");
                    pst.setString(1, fullName);
                    pst.setString(2, email);
                    pst.setString(3, country);
                    pst.setString(4, userid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Updated!");
                    table_load();
                    txtName.setText("");
                    txtEmail.setText("");
                    txtCountry.setText("");
                    txtName.requestFocus();
                }
                catch(SQLException e1)
                {
                    e1.printStackTrace();
                }

            }
        });

        //Delete
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userId;
                userId = txtSearch.getText();

                try{
                    //pst = con.prepareStatement("delete from users where Id = ?");
                    pst = con.prepareStatement("DELETE FROM list_of_users.users where Id=?;");
                    pst.setString(1, userId);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Deleted!");
                    table_load();
                    txtName.setText("");
                    txtEmail.setText("");
                    txtCountry.setText("");
                    txtName.requestFocus();

                    }catch(SQLException e1)
                {
                    e1.printStackTrace();
                }

            }
        });

        //Clear
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtName.setText("");
                txtEmail.setText("");
                txtCountry.setText("");
                txtSearch.setText("");

            }
        });
    }
}
