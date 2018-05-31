import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import java.awt.event.*;


class diskprog extends JFrame
{
	JPanel jp1;
	JTable tab1;
	int extstate;
	diskprog()
	{
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		extstate = getExtendedState();
		setTitle("Disk Information Application");
		setLayout(new BorderLayout());
		jp1 = new JPanel();
		JTabbedPane jtb = new JTabbedPane();
		jp1.setLayout(new BorderLayout());
		ImageIcon viewi = new ImageIcon("images\\view_icon.png");
		ImageIcon searchi = new ImageIcon("images\\search_icon.png");
		ImageIcon addi = new ImageIcon("images\\add_icon.png");
		ImageIcon modifyi = new ImageIcon("images\\modify_icon.png");

		jtb.addTab("View Record" , viewi , new viewr());
		jtb.addTab("Search Record" , searchi , new searchr());
		jtb.addTab("Add Record" , addi , new addr());
		jtb.addTab("Modify Record List" , modifyi , new modifyr());
		jp1.add(jtb);
		add(jp1 , BorderLayout.NORTH);
		
	}
	
	public static void main(String args[])
	{
		diskprog c1 = new diskprog();
		c1.setIconImage(Toolkit.getDefaultToolkit().getImage("images\\logo.png"));
		c1.setVisible(true);
	}
}

class viewr extends JPanel implements ActionListener
{
	Font t_font ,l_font;
	DefaultTableModel tm = new DefaultTableModel();
	JTable view_tabl = new JTable();
	JButton refresh = new JButton("Refresh");
	int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
	int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
	JScrollPane scroll = new JScrollPane(view_tabl , v ,h);


	public viewr()
	{
		setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		view_tabl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setPreferredSize(new Dimension(screenSize.width,(screenSize.height-135)));
		add(scroll);
		t_font=new Font("Times New Roman" , Font.PLAIN , 22);
		l_font=new Font("Times New Roman" , Font.BOLD , 24 );
		String[] columnNames = {"SR. NO.", "Disk Burn no." , "Contents"};
		tm.setColumnIdentifiers(columnNames);
		view_tabl.setFont(t_font);
		view_tabl.setRowHeight(40);
		refresh.setFont(t_font);
		refresh.addActionListener(this);
		add(refresh , BorderLayout.SOUTH);
		view_tabl.setModel(tm);

		TableColumn column1 = view_tabl.getColumn("SR. NO.");
		TableColumn column2 = view_tabl.getColumn("Disk Burn no.");
		//TableColumn column3 = view_tabl.getColumn("Contents");
        column1.setMinWidth(140);
        column1.setMaxWidth(140);
        column2.setMinWidth(140);
        column2.setMaxWidth(140);
		view_tabl.setFocusable(false);
		view_tabl.setRowSelectionAllowed(true);
		view_tabl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		this.ref();
	}

	public void ref()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cd_info","root","pass");
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from cd_table");

			while (rs.next())
			{
  	          tm.addRow(new Object[]{rs.getString(1) , rs.getString(2) , rs.getString(3) });
	        }
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		int rows = tm.getRowCount();
		for(int i = rows - 1; i >=0; i--)
		{
		   tm.removeRow(i);
		}
		this.ref();
	}
}

class searchr extends JPanel implements ActionListener
{
	JLabel l1 , l2;
	JTextField jtf;
	JTable search_tabl = new JTable();
	DefaultTableModel tm = new DefaultTableModel();
	JButton search = new JButton("Search");
	JPanel np = new JPanel();
	int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
	int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
	JScrollPane scroll = new JScrollPane(search_tabl , v ,h);
	Font t_font ,l_font;
	int flag = 0;
	
	public searchr()
	{
		setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screenSize.width,(screenSize.height-135)));
		l1 = new JLabel("Enter the Contents to search:=");
		l2 = new JLabel();
		jtf = new JTextField(10);
		t_font=new Font("Times New Roman" , Font.PLAIN , 22);
		l_font=new Font("Times New Roman" , Font.BOLD , 24 );
		l1.setFont(l_font);
		jtf.setFont(t_font);
		np.setLayout(new FlowLayout());
		String[] columnNames = {"SR. NO.", "Disk Burn no." , "Contents"};
		tm.setColumnIdentifiers(columnNames);
		search_tabl.setFont(t_font);
		search_tabl.setRowHeight(40);
		//search.setFont(t_font);
		np.add(l1);
		np.add(jtf);
		np.add(search);
		np.add(l2);
		add(scroll);
		
		search.addActionListener(this);
		add(np , BorderLayout.NORTH);
		search_tabl.setModel(tm);

		TableColumn column1 = search_tabl.getColumn("SR. NO.");
		TableColumn column2 = search_tabl.getColumn("Disk Burn no.");
		//TableColumn column3 = view_tabl.getColumn("Contents");
        column1.setMinWidth(140);
        column1.setMaxWidth(140);
        column2.setMinWidth(140);
        column2.setMaxWidth(140);
		search_tabl.setFocusable(false);
		search_tabl.setRowSelectionAllowed(true);
		search_tabl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	}
	public void actionPerformed(ActionEvent e)
	{
		flag = 0;
		int rows = tm.getRowCount();
		for(int i = rows - 1; i >=0; i--)
		{
		   tm.removeRow(i);
		}
		if(jtf.getText().equals(""))
		{
			l2.setText("Please Enter the contents of disk to be search");	
		}
		else
		{
		try
		{
			l2.setText(" ");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cd_info","root","pass");
			Statement stmt=con.createStatement();

			ResultSet rs=stmt.executeQuery("select * from cd_table where contents like "+ "'%"+jtf.getText()+"%' ");

			while (rs.next())
			{
				tm.addRow(new Object[]{rs.getString(1) , rs.getString(2) , rs.getString(3) });
				flag = 1;
			}
			con.close();
		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}
		if(flag == 0)
			l2.setText("No Record Found");
		}
	}
}

class addr extends JPanel implements ActionListener
{
	JLabel sr_l ,burn_l ,cont_l , save_l, res;
	JTextField sr_tf , burn_tf ;
	JTextArea cont_ta;
	JPanel p1 ,p2;
	JButton save_b;
	Font t_font ,l_font;
	int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
	int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
	JScrollPane scroll;
	int b;
	
	public addr()
	{
		setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screenSize.width,(screenSize.height-135)));
		t_font=new Font("Times New Roman" , Font.PLAIN , 22);
		l_font=new Font("Times New Roman" , Font.BOLD , 24 );
		
		sr_l = new JLabel("Enter Serial NO. of Disk =");
		burn_l = new JLabel("Enter Disk Burn NO. =");
		cont_l = new JLabel("Enter the Disk Contents =");
		res = new JLabel("");
		sr_tf = new JTextField("");
		burn_tf = new JTextField("");
		cont_ta = new JTextArea("");
		save_b = new JButton("Save Record");
		sr_l.setFont(l_font);
		burn_l.setFont(l_font);
		cont_l.setFont(l_font);
		res.setFont(l_font);
		sr_tf.setFont(t_font);
		burn_tf.setFont(t_font);
		cont_ta.setFont(t_font);
		save_b.setFont(t_font);
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setPreferredSize(new Dimension(100,100));
		res.setBounds(800 , 600 , 400 , 30);
		sr_l.setBounds(80 ,80 , 275 ,30);
		sr_tf.setBounds(370 , 80 , 185 , 30);
		burn_l.setBounds(80 , 140 , 250 , 30 );
		burn_tf.setBounds(370 , 140 , 185 , 30);
		cont_l.setBounds(80 , 200 , 270 , 30);
		cont_ta.setLineWrap(true);
		scroll = new JScrollPane(cont_ta , v ,h);
		scroll.setBounds(370 , 200 , 800 , 140);
		save_b.setBounds(970 , 460 , 200 ,30);
		p1.add(sr_l);
		p1.add(sr_tf);
		p1.add(burn_l);
		p1.add(burn_tf);
		p1.add(cont_l);
		p1.add(scroll);
		p1.add(save_b);
		p1.add(res);
		add(p1);
		save_b.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		b = 0;
		if(sr_tf.getText().equals("") || burn_tf.getText().equals("") || cont_ta.getText().equals(""))
		{
			res.setText("Please enter valid information of disk");
		}
		else
		{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cd_info","root","pass");
			Statement stmt=con.createStatement();

			b=stmt.executeUpdate("insert into cd_table values("+sr_tf.getText()+", '"+burn_tf.getText()+"' , '"+cont_ta.getText()+"')");
		
			if(b!=0)
			{
				res.setText("Record Number "+sr_tf.getText()+" is saved successfully");
				sr_tf.setText("");
				burn_tf.setText("");
				cont_ta.setText("");
			}
			else
			{
				res.setText("Unable to save record");
			}
			con.close();
		}
		catch(Exception ex)
		{
			System.out.println(ex);
			res.setText("Unable to save record");
		}
		}
	}
}

class modifyr extends JPanel implements ActionListener
{
	JLabel sr_l ,burn_l ,cont_l , save_l, res , search_l , serr_l;
	JTextField sr_tf , burn_tf ,search_tf ;
	JTextArea cont_ta;
	JPanel p1 ,p2 , p3;
	JButton save_b , search_b , delete_b;
	Font t_font ,l_font;
	int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
	int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
	JScrollPane scroll;
	int b = 0 , d = 0;
	
	public modifyr()
	{
		setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screenSize.width,(screenSize.height-135)));
		t_font=new Font("Times New Roman" , Font.PLAIN , 22);
		l_font=new Font("Times New Roman" , Font.BOLD , 24 );
		
		sr_l = new JLabel("Serial NO. = ");
		burn_l = new JLabel("Disk Burn NO. = ");
		cont_l = new JLabel("Contents = ");
		search_l = new JLabel("Enter Serial NO. of Disk :=  ");
		res = new JLabel("");
		serr_l = new JLabel("");
		sr_tf = new JTextField("");
		burn_tf = new JTextField("");
		cont_ta = new JTextArea("");
		search_tf = new JTextField(10);
		search_b = new JButton("Select Record");
		save_b = new JButton("Modify Record");
		delete_b = new JButton("Delete Record");
		sr_l.setFont(l_font);
		burn_l.setFont(l_font);
		cont_l.setFont(l_font);
		search_l.setFont(l_font);
		res.setFont(l_font);
		//serr_l.setFont(l_font);
		sr_tf.setFont(t_font);
		burn_tf.setFont(t_font);
		cont_ta.setFont(t_font);
		search_tf.setFont(t_font);
		search_b.setFont(t_font);
		save_b.setFont(t_font);
		delete_b.setFont(t_font);
		p1 = new JPanel();
		p3 = new JPanel();
		p1.setLayout(null);
		p3.setLayout(new FlowLayout());
		p1.setPreferredSize(new Dimension(100,100));
		res.setBounds(800 , 600 , 1050 , 60);
		sr_l.setBounds(80 ,80 , 275 ,30);
		sr_tf.setBounds(370 , 80 , 185 , 30);
		burn_l.setBounds(80 , 140 , 250 , 30 );
		burn_tf.setBounds(370 , 140 , 185 , 30);
		cont_l.setBounds(80 , 200 , 270 , 30);
		cont_ta.setLineWrap(true);
		scroll = new JScrollPane(cont_ta , v ,h);
		scroll.setBounds(370 , 200 , 800 , 140);
		save_b.setBounds(970 , 460 , 200 ,30);
		delete_b.setBounds(970 , 560 , 200, 30);
		p3.add(search_l);
		p3.add(search_tf);
		p3.add(search_b);
		p3.add(serr_l);
		p1.add(sr_l);
		p1.add(sr_tf);
		p1.add(burn_l);
		p1.add(burn_tf);
		p1.add(cont_l);
		p1.add(scroll);
		p1.add(save_b);
		p1.add(delete_b);
		p1.add(res);
		add(p3 , BorderLayout.NORTH);
		add(p1);
		
		search_b.addActionListener(this);
		save_b.addActionListener(this);
		delete_b.addActionListener(this);
		
		
	}
	public void actionPerformed(ActionEvent e)
	{
		String sel;
		sel = e.getActionCommand();
		if(sel.equals("Select Record"))
		{
			res.setText("");
			if(search_tf.getText().equals(""))
			{
				serr_l.setText("Please Enter valid serial NO.");
			}
			else
			{
				try
				{
					serr_l.setText("");
					Class.forName("com.mysql.jdbc.Driver");
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cd_info","root","pass");
					Statement stmt=con.createStatement();
					
					ResultSet rs=stmt.executeQuery("select * from cd_table where `Sr Num` = "+search_tf.getText());


					if(rs.next())
					{
						sr_tf.setText(rs.getString(1));
						burn_tf.setText(rs.getString(2));
						cont_ta.setText(rs.getString(3));
					}
					else
					{
						serr_l.setText("No Record Found");
					}
					con.close();
				}
				catch(Exception ex)
				{
					System.out.println(ex);
					serr_l.setText("Please Enter valid serial NO.");
				}	
			}		
		}
	
		if(sel.equals("Modify Record"))
		{
			serr_l.setText("");
			if(search_tf.getText().equals(""))
			{
				serr_l.setText("Please Select the record first");
			}
			else
			{
			
			int n = JOptionPane.showConfirmDialog(null , "Are You Sure to Modify Record number "+search_tf.getText()+"?" , "Modify "  , JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE);
			if(n == 0)
			{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cd_info","root","pass");
				Statement stmt=con.createStatement();

				b=stmt.executeUpdate("UPDATE `cd_table` SET `Sr Num`="+sr_tf.getText()+ " , "+"`Disk burn no.`='"+burn_tf.getText()+"' , "+"`Contents`='"+cont_ta.getText()+"' WHERE `Sr Num`= "+search_tf.getText());       
		
				if(b!=0)
				{
					res.setText("Record number "+search_tf.getText()+" is updated successfully");
					sr_tf.setText("");
					burn_tf.setText("");
					cont_ta.setText("");
					search_tf.setText("");
				}
				else
				{
					res.setText("Unable to save record(Please Check record Details)");
				}
			con.close();
			}
			catch(Exception ex)
			{
				System.out.println(ex);
				res.setText("Unable to save record(Please Check record Details) "+ex);
			}
			}
			}
		}
		if(sel.equals("Delete Record"))
		{
			serr_l.setText("");
			if(search_tf.getText().equals(""))
			{
				serr_l.setText("Please Select the record first");
			}
			else
			{
			int n = JOptionPane.showConfirmDialog(null , "Are You Sure to Delete Record number "+search_tf.getText()+"?" , "Delete "  , JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE);
			if(n == 0)
			{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cd_info","root","pass");
				Statement stmt=con.createStatement();
				
				d=stmt.executeUpdate("DELETE FROM `cd_table` WHERE `Sr Num`= "+search_tf.getText());
				
				if(d!=0)
				{
					res.setText("Record number "+search_tf.getText()+" is Deleted Successfully");
					sr_tf.setText("");
					burn_tf.setText("");
					cont_ta.setText("");
					search_tf.setText("");
				}
				con.close();
			}
			catch(Exception ex)
			{
				System.out.println(ex);
				res.setText("Unable to Delete record  "+ex);
			}
			}
			}
		}
	}
}
