package Assignment_5;

import com.mongodb.*;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Search extends JFrame {
    String [] Sorting   ={"IMDB rating", "Number of award wins", "Year","Runtime","Votes"};
    String [] Searching ={"Movie Title","Actor Name","Director Name","Genre"};
    JComboBox<String> Sort_CB, Search_CB;
    JButton Search_B,Comments_B, Back_B;
    JTextField Text;
    JPanel main_Search,logo_p;
    JTable jTable1;
    JScrollPane jScrollPane1;
    JTextArea Comments;
    JScrollPane Comment_Pane;
    JLabel Search_By,Sort_By;

    public Search(){
        main_Search=new JPanel();

        logo_p = new JPanel() {@Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            ImageIcon img = new ImageIcon("logo.png");
            g.drawImage(img.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        }};
        logo_p.setBounds(100,100,160,100);
        logo_p.setBackground(new Color(0,0,0));
        main_Search.add(logo_p);

        main_Search.setBackground(new Color(0,0,0));
        setSize(1240, 700);
        setTitle("IMDb");
        add(main_Search);
        main_Search.setLayout(null);
//        main_Search.setBackground(new Color(153,153,153));

        Search_By = new JLabel("Search By: ");
        Search_By.setBounds(500, 150, 100, 25);
        Search_By.setFont(new Font("Serif", Font.BOLD,15));
        Search_By.setForeground(Color.white);
        main_Search.add(Search_By);

        Search_CB = new JComboBox<String>(Searching);
        Search_CB.setBounds(600,150,150,30);
        main_Search.add(Search_CB);

        Sort_By = new JLabel("Sort By: ");
        Sort_By.setBounds(500, 200, 100, 25);
        Sort_By.setFont(new Font("Serif", Font.BOLD,15));
        Sort_By.setForeground(Color.white);
        main_Search.add(Sort_By);

        Sort_CB = new JComboBox<String>(Sorting);
        Sort_CB.setBounds(600,200,150,30);
        main_Search.add(Sort_CB);

        Text = new JTextField(20);
        Text.setBounds(260,250,300,30);
        Text.setFont(new Font("Serif", Font.BOLD,15));
        main_Search.add(Text);

        Comments_B = new JButton("Comments");
        Comments_B.setBounds(700,250,130,25);
        Comments_B.setBackground(Color.BLUE);
        Comments_B.setForeground(Color.white);
        Comments_B.setFont(new Font("Serif", Font.BOLD,15));
        Comments_B.setVisible(false);
        main_Search.add(Comments_B);

        Search_B = new JButton("Search");
        Search_B.setBounds(590,250,100,25);
        Search_B.setBackground(Color.BLUE);
        Search_B.setForeground(Color.white);
        Search_B.setFont(new Font("Serif", Font.BOLD,15));
        main_Search.add(Search_B);

        Comments = new JTextArea();
        Comments.setLineWrap(true);
        Comments.setWrapStyleWord(true);
        Comments.setEditable(false);
        Comments.setFont(new Font("Serif", Font.PLAIN,15));
        Comment_Pane = new JScrollPane(Comments);
        Comment_Pane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        Comment_Pane.setBounds(100, 300, 1000, 350);
        Comment_Pane.setVisible(false);
        main_Search.add(Comment_Pane);

        jScrollPane1=new JScrollPane();


        Back_B = new JButton("Back");
        Back_B.setBounds(20,20,100,25);
        Back_B.setBackground(Color.BLUE);
        Back_B.setForeground(Color.white);
        Back_B.setFont(new Font("Serif", Font.BOLD,15));
        main_Search.add(Back_B);

        setVisible(true);
        MyListener listener = new MyListener();
        Search_B.addActionListener(listener);
        Comments_B.addActionListener(listener);
        Back_B.addActionListener(listener);

    }
    public class MyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("Search") ) {
                if (!Text.getText().isEmpty()){
                    try {
                        MongoClient mongoclient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
                        DB mydb = mongoclient.getDB("imdb");
                        DBCollection mycollection = mydb.getCollection("movies");

                        BasicDBObject query = new BasicDBObject(Get_Search(Search_CB.getSelectedItem().toString()),java.util.regex.Pattern.compile(Text.getText(), Pattern.CASE_INSENSITIVE));
                        DBCursor cursor1 = mycollection.find(query);


                        if (!cursor1.hasNext()) {
                            JOptionPane.showMessageDialog(null, Search_CB.getSelectedItem().toString()+" does not exist");

                        } else  {
                            BasicDBObject sorting = null;
                            sorting = new BasicDBObject(Get_Sorting(Sort_CB.getSelectedItem().toString()), 1);
                            DBCursor cursor = mycollection.find(query).sort(sorting);
                            JOptionPane.showMessageDialog(null, "sorted with "+Sort_CB.getSelectedItem().toString());
                            display(cursor);

                            jScrollPane1.setBounds(100, 300, 1000, 350);
                            main_Search.add(jScrollPane1);
                            jScrollPane1.setVisible(true);
                            Comment_Pane.setVisible(false);
                            Comments_B.setVisible(true);


                        }
                    } catch (Exception ae) {
                        System.out.println(ae.toString());
                        ae.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Enter text to search");
                }
            } if (e.getActionCommand().equalsIgnoreCase("Comments")) {

                        int movie_row = jTable1.getSelectedRow();
                        System.out.println("srow: " + movie_row);

                        if (movie_row == -1) {
                            JOptionPane.showMessageDialog(null, "Select a rows!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            jScrollPane1.setVisible(false);
                            Comment_Pane.setVisible(true);

                            TableModel model = jTable1.getModel();

                            String m_title = model.getValueAt(movie_row, 0).toString();
                            Comments.setText(id_to_comment(title_to_id_movie(m_title)));
                        }


            }if (e.getActionCommand().equalsIgnoreCase("Back")) {
                dispose();
                new Main_Menu();
            }


        }
    }
    private void display(DBCursor cursor) {

        jTable1 = new javax.swing.JTable();
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                 new Object [][] {
                                 },
                                 new String [] {
                                         "Title", "Year", "Runtime", "Plot", "Type", "Directors", "IMBD rating", "Votes", "Countries", "Genres"
                                 }
                         )
                         {
                             Class[] columnTypes = new Class[]{
                                     String.class, String.class, String.class, String.class,String.class,
                                     String.class, String.class, String.class, String.class,String.class
                             };
                             public Class getColumnClass(int columnIndex) {
                                 return columnTypes[columnIndex];
                             }
                             boolean[] columnEditables = new boolean[]{
                                     false, false, false, false,false,false, false, false, false,false
                             };
                             public boolean isCellEditable(int row, int column) {
                                 return columnEditables[column];
                             }
                         }
        );
        jScrollPane1.setViewportView(jTable1);
        DefaultTableModel tbModel = (DefaultTableModel) jTable1.getModel();

        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            Object Title = document.get("title");
            Object Year = document.get("year");
            Object Runtime = document.get("runtime");
            Object Plot = document.get("plot")+"\n\n";
            Object Type = document.get("type");
            Object Directors = document.get("directors");
            DBObject imbdV = (DBObject) document.get("imdb");
            Object IMBDRating = imbdV.get("rating");
            Object Votes = imbdV.get("votes");
            Object Countries = document.get("countries");
            Object Genres = document.get("genres");
            Object tbData[] = {Title, Year, Runtime, Plot, Type, Directors, IMBDRating, Votes, Countries, Genres};
            Object Image= document.get("poster");
            tbModel.addRow(tbData);
            System.out.println(Title);
        }
    }

    public String Get_Sorting(String Sorted){
        if (Sorted.equals("IMDB rating")){ return "imdb.rating";}
        else if (Sorted.equals("Number of award wins")){ return "awards.wins"; }
        else if (Sorted.equals("Year")){ return "year"; }
        else if (Sorted.equals("Runtime")){ return "runtime"; }
        else{ return "imdb.votes"; }
    }

    public String Get_Search (String Selected){
        if (Selected.equals("Movie Title")){ return "title";}
        else if (Selected.equals("Actor Name")){ return "cast"; }
        else if (Selected.equals("Director Name")){ return "directors"; }
       else{ return "genres"; }
    }

    public  static String title_to_id_movie(String title){
        String id="";
        try {
            MongoClient mongoclient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            DB mydb = mongoclient.getDB("imdb");
            DBCollection mycollection = mydb.getCollection("movies");
            BasicDBObject query = new BasicDBObject("title", title);
            DBCursor cursor1 = mycollection.find(query);
            while (cursor1.hasNext())
            {
                DBObject document = cursor1.next();
                return String.valueOf(document.get("_id"));
            }
        }
        catch (Exception e)
        {
        }
        return id;
    }



    public static String id_to_comment(String id){
        StringBuilder comment= new StringBuilder();
        try {
            MongoClient mongoclient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            DB mydb = mongoclient.getDB("imdb");
            DBCollection mycollection = mydb.getCollection("comments");
            BasicDBObject query = new BasicDBObject("movie_id",new ObjectId( id));
            DBCursor cursor1 = mycollection.find(query);
            int c=1;
            while (cursor1.hasNext())
            {
                System.out.println("hr");
                DBObject document = cursor1.next();
                comment.append(c).append(".\n").append(String.valueOf(document.get("text"))).append("\nCOMMENTED BY -").append(document.get("name")).append("\n").append("----------").append("--------------------------------").append("------------------------------------------------------------\n");
                c++;

            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }if (comment.toString().equals(""))
            return "no comments for this movie";
        return comment.toString();
    }

    public static void main(String[] args) {
        new Search();
    }
}
