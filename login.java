import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.ImageIcon;
import java.awt.Image;

public class login {
    int id;
	JButton playButton;
	private Clip clip; 
    private boolean isPlaying = false; 

    public void loginView() throws SQLException {
		
		SQLoperations manage = new SQLoperations();
		JFrame frame = new JFrame();
        frame.setTitle("--Organization--");
		frame.setSize(700, 500);
		frame.setLayout(null);
		frame.getContentPane().setBackground( Color.decode("#BE398D"));
		frame.setLocationRelativeTo(null);

		//For Music Player
		ImageIcon noteIcon = new ImageIcon("media/note.png");
		Image scaledImage = noteIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        noteIcon = new ImageIcon(scaledImage);

		playButton = new JButton(noteIcon);
		playButton.setBounds(620, 400, 50, 50);
		playButton.setContentAreaFilled(false);
        playButton.setBorder(null);
		playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the button click event
                toggleMusic();
            }
        });
		frame.add(playButton);
		playMusic();


		// Load GIF from file or URL
        URL gifUrl = getClass().getResource("/images/duck.png");
        ImageIcon gifIcon = new ImageIcon(gifUrl);

		// Resize the image
        Image resizedImage = gifIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // Create a JLabel to display the GIF
        JLabel gifLabel = new JLabel(resizedIcon);
        gifLabel.setBounds(265, 110, 150, 150);
        frame.add(gifLabel);
		
		JLabel heading = new JLabel("Survey Management System");
		heading.setBounds(0, 50, 700, 50);
		heading.setHorizontalAlignment(JLabel.CENTER);
		heading.setFont(new Font("Times New Roman", Font.BOLD, 40));
		frame.add(heading);

		/*Guest survey Option */
		JButton attend = new JButton("Take the Survey!");
        attend.setBounds(240, 250, 200, 50);
        frame.add(attend);
        attend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String surveyCode = JOptionPane.showInputDialog("Please Enter the Unique Survey Code : ");
				try {
					if(!surveyCode.isEmpty() && surveyCode.length() == 5) {
						if(manage.check(surveyCode)) {
							guest guest = new guest();
							guest.guestView(surveyCode);
						}
						else {
							JOptionPane.showMessageDialog(frame, "Incorrect Survey Code, please try again.", "Warning Message", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				catch(Exception e2) {
					
				}
			}
		});

		JButton adminButton = new JButton("Admin?");
        adminButton.setBounds(180, 310, 150, 40);
        frame.add(adminButton);
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminLoginView();
            }
        });

		/*Signup section option */
		JButton signUp = new JButton("Sign up as Admin?");
		signUp.setBounds(360, 310, 150, 40);
		frame.add(signUp);
		signUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				signup signup = new signup();
				signup.signUpView();
			}
		});
		
		frame.setVisible(true);
	}

	private void playMusic() {
        try {
            String musicFilePath = "media/Alan Walker - Faded (Instrumental Version).wav";
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File(musicFilePath).getAbsoluteFile());

            // If already playing, stop the music
            stopMusic();

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            isPlaying = true;

            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        stopMusic();
                    }
                }
            });
            updateButtonIcon();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	private void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
        isPlaying = false;
        updateButtonIcon();

    }

    private void toggleMusic() {
        if (isPlaying) {
            stopMusic();
        } else {
            playMusic();
        }
    }

    private void updateButtonIcon() {
        if (isPlaying) {
            ImageIcon noteOffIcon = new ImageIcon("media/noteOff.png");
            Image scaledImage = noteOffIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            noteOffIcon = new ImageIcon(scaledImage);
            playButton.setIcon(noteOffIcon);
        } else {
            ImageIcon noteIcon = new ImageIcon("media/note.png");
            Image scaledImage = noteIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            noteIcon = new ImageIcon(scaledImage);
            playButton.setIcon(noteIcon);
        }
    }

	private void openAdminLoginView() {
        JFrame adminFrame = new JFrame("Admin Login");
        adminFrame.setSize(380, 250);
        adminFrame.setLayout(null);
        adminFrame.setLocationRelativeTo(null);
		adminFrame.getContentPane().setBackground( Color.decode("#BE398D"));

        JLabel adminLabel = new JLabel("Admin Login");
        adminLabel.setBounds(150, 30, 100, 30);
        adminFrame.add(adminLabel);

        JLabel uname = new JLabel("Username : ");
        uname.setBounds(50, 80, 80, 30);
        adminFrame.add(uname);

        JTextField name = new JTextField();
        name.setBounds(150, 80, 200, 30);
        adminFrame.add(name);

        JLabel upass = new JLabel("Password : ");
        upass.setBounds(50, 120, 80, 30);
        adminFrame.add(upass);

        JPasswordField pass = new JPasswordField();
        pass.setBounds(150, 120, 200, 30);
        adminFrame.add(pass);

        JButton login = new JButton("Login");
        login.setBounds(150, 170, 100, 30);
        adminFrame.add(login);
        login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = name.getText();
				String password = new String(pass.getPassword());
				if(username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(adminFrame, "Please Enter All Information.", "Warning Message", JOptionPane.WARNING_MESSAGE);
				}
				else {
					try {
						SQLoperations manage= new SQLoperations();
						id = manage.authUser(username, password);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					if (id == -1) {
						JOptionPane.showMessageDialog(adminFrame, "User Does Not Exist.", "Warning Message", JOptionPane.WARNING_MESSAGE);
					}
					else if(id == 0) {
						JOptionPane.showMessageDialog(adminFrame, "Incorrect Password, please try again.", "Warning Message", JOptionPane.WARNING_MESSAGE);
					}
					else {
						mainpage mainPage = new mainpage();
						try {
							mainPage.mainPageView(id);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						
					}
					adminFrame.dispose();
				}
			}
		});

        adminFrame.setVisible(true);
    }
}
