package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import model.DAO;
import utils.Validador;

public class IdentidadeQrcode extends JFrame {

	//instanciar objetos
	DAO dao = new DAO();
	
	
	
	
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblStatus;
	private JPanel panel;
	private JLabel lblData;
	private JLabel lblNewLabel;
	private JTextField txtID;
	private JTextField txtNome;
	private JTextField txtNumero;
	private JTextField txtMensagem;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblFoto;
	private JButton btnBuscar;
	private JButton btnExcluir;
	private JButton btnSalvar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IdentidadeQrcode frame = new IdentidadeQrcode();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IdentidadeQrcode() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				status();
				setarData();
			}
		});
		setTitle("Identidade QRcode");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(IdentidadeQrcode.class.getResource("/IMG/9113452_qrcode_solid_icon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 410);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(new Color(138, 43, 226));
		panel.setBounds(0, 326, 804, 45);
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblStatus = new JLabel("");
		lblStatus.setIcon(new ImageIcon(IdentidadeQrcode.class.getResource("/IMG/dboff.png")));
		lblStatus.setBounds(732, 11, 32, 32);
		panel.add(lblStatus);
		
		lblData = new JLabel("");
		lblData.setBounds(10, 11, 407, 23);
		panel.add(lblData);
		lblData.setForeground(SystemColor.text);
		lblData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblNewLabel = new JLabel("ID");
		lblNewLabel.setBounds(10, 23, 46, 14);
		contentPane.add(lblNewLabel);
		
		txtID = new JTextField();
		txtID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0123456789";
				if(!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}
		});
		txtID.setBounds(83, 20, 86, 20);
		contentPane.add(txtID);
		txtID.setColumns(10);
		
		lblNewLabel_1 = new JLabel("Nome");
		lblNewLabel_1.setBounds(10, 64, 46, 20);
		contentPane.add(lblNewLabel_1);
		
		txtNome = new JTextField();
		txtNome.setBounds(83, 64, 250, 20);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		// Limitar caracteres PlainDocument
		txtNome.setDocument(new Validador(30));
		
		lblNewLabel_2 = new JLabel("Tel");
		lblNewLabel_2.setBounds(10, 109, 33, 14);
		contentPane.add(lblNewLabel_2);
		
		txtNumero = new JTextField();
		txtNumero.setBounds(83, 106, 155, 20);
		contentPane.add(txtNumero);
		txtNumero.setColumns(10);
			
		
		lblNewLabel_3 = new JLabel("Mensagem");
		lblNewLabel_3.setBounds(10, 153, 69, 14);
		contentPane.add(lblNewLabel_3);
		
		txtMensagem = new JTextField();
		txtMensagem.setBounds(83, 149, 386, 23);
		contentPane.add(txtMensagem);
		txtMensagem.setColumns(10);
		
		JButton btnAdicionar = new JButton("");
		btnAdicionar.setToolTipText("Gerar QRcode");
		btnAdicionar.setIcon(new ImageIcon(IdentidadeQrcode.class.getResource("/img/qrcode_plus_icon_137272 (1).png")));
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});
		btnAdicionar.setBounds(31, 213, 48, 48);
		contentPane.add(btnAdicionar);
		
		lblFoto = new JLabel("");
		lblFoto.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblFoto.setBounds(484, 10, 300, 300);
		contentPane.add(lblFoto);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarID();
			}
		});
		btnBuscar.setForeground(Color.BLACK);
		btnBuscar.setBounds(213, 19, 120, 23);
		contentPane.add(btnBuscar);
		
		JButton btnLimpar = new JButton("Limpar campos");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparCampos();
			}
		});
		btnLimpar.setBounds(343, 213, 126, 23);
		contentPane.add(btnLimpar);
		
		JButton btnEditar = new JButton("");
		btnEditar.setIcon(new ImageIcon(IdentidadeQrcode.class.getResource("/img/qrcode_edit_icon_138259 (1).png")));
		btnEditar.setToolTipText("Editar Cadastro");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editar();
			}
		});
		btnEditar.setBounds(111, 213, 48, 48);
		contentPane.add(btnEditar);
		
		btnExcluir = new JButton("");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluir();
			}
		});
		btnExcluir.setIcon(new ImageIcon(IdentidadeQrcode.class.getResource("/img/qrcode_remove_icon_137271 (1).png")));
		btnExcluir.setToolTipText("Excluir");
		btnExcluir.setBounds(197, 213, 48, 48);
		contentPane.add(btnExcluir);
		
		btnSalvar = new JButton("");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarQRCode();
			}
		});
		btnSalvar.setIcon(new ImageIcon(IdentidadeQrcode.class.getResource("/img/save_78935.png")));
		btnSalvar.setToolTipText("Salvar QRcode");
		btnSalvar.setBounds(421, 262, 48, 48);
		contentPane.add(btnSalvar);
	}// fim construtor
	
	private void status() {
		try {
			con = dao.conectar();
			if (con == null) {
				//System.out.println("Erro de conexão");
				lblStatus.setIcon(new ImageIcon
(IdentidadeQrcode.class.getResource("/img/dboff.png")));
			} else {
				//System.out.println("Banco de dados conectado");
				lblStatus.setIcon(new ImageIcon
(IdentidadeQrcode.class.getResource("/img/dbon.png")));
				con.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void setarData() {
		Date data = new Date();
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
		lblData.setText(formatador.format(data));
	}
	
	private BufferedImage generateQRCodeImage(String data) throws Exception {
	    int width = 300; 
	    int height = 300;

	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = qrCodeWriter.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, width, height);

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
	    byte[] imageInBytes = baos.toByteArray();
	    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageInBytes));

	    return image;
	}
	
	private void adicionar() {
		String insert = "insert into dadosusuarios(nomeusuario, telefoneusuario, mensagem, qrcodecriado) values(?,?,?,?)";
		
		String qrcodeCriado =( txtNome.getText() + "/n" + txtNumero.getText() + "/n" + txtMensagem.getText());
		
	
		try {
			con = dao.conectar();
			pst = con.prepareStatement(insert);
			pst.setString(1, txtNome.getText());
			pst.setString(2, txtNumero.getText());
			pst.setString(3, txtMensagem.getText());
			
			
			BufferedImage qrCodeImage = generateQRCodeImage(qrcodeCriado);

	        // Convert BufferedImage to Blob
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(qrCodeImage, "PNG", baos);
	        byte[] imageInBytes = baos.toByteArray();
	        Blob qrCodeBlob = (Blob) con.createBlob();
	        qrCodeBlob.setBytes(1, imageInBytes);
	        
			pst.setBlob(4,qrCodeBlob);
			
			int confirma = pst.executeUpdate();
			if (confirma == 1) {
				JOptionPane.showMessageDialog(null, "QR code gerado com sucesso!");
				
				 lblFoto.setIcon(new ImageIcon(qrCodeImage));
			}else {
				JOptionPane.showMessageDialog(null, "Erro ao gerar o QR code");
			}
			
			con.close();
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	private void buscarID() {
		if(txtID.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "digite o ID");
			txtID.requestFocus();
		}else {
			String readID = "select * from dadosusuarios where id = ?";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(readID);
				pst.setString(1, txtID.getText());
				rs = pst.executeQuery();
				if (rs.next()) {
					txtNome.setText(rs.getString(2));
					txtNumero.setText(rs.getString(5));
					txtMensagem.setText(rs.getString(3));
					Blob blob = (Blob) rs.getBlob(4);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);
					}
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH));
					lblFoto.setIcon(foto);
				} else {
					JOptionPane.showMessageDialog(null, "Usuário não cadastrado" );
				}
				con.close();
			} catch (Exception e) {
				System.out.println();
			}
		}
	}
	
	private void editar() {
		String update = "update dadosusuarios set nomeusuario=?, telefoneusuario=?, mensagem=?, qrcodecriado=? where id=? ";
		
		String qrcodeCriado =( txtNome.getText() + "/n" + txtNumero.getText() + "/n" + txtMensagem.getText());
		
	
		try {
			con = dao.conectar();
			pst = con.prepareStatement(update);
			pst.setString(1, txtNome.getText());
			pst.setString(2, txtNumero.getText());
			pst.setString(3, txtMensagem.getText());
			pst.setString(5, txtID.getText());
			
			BufferedImage qrCodeImage = generateQRCodeImage(qrcodeCriado);

	        // Convert BufferedImage to Blob
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(qrCodeImage, "PNG", baos);
	        byte[] imageInBytes = baos.toByteArray();
	        Blob qrCodeBlob = (Blob) con.createBlob();
	        qrCodeBlob.setBytes(1, imageInBytes);
	        
			pst.setBlob(4,qrCodeBlob);
			
			int confirma = pst.executeUpdate();
			if (confirma == 1) {
				JOptionPane.showMessageDialog(null, "QR code atualizado com sucesso!");
				
				 lblFoto.setIcon(new ImageIcon(qrCodeImage));
			}else {
				JOptionPane.showMessageDialog(null, "Erro ao atualizar o QR code");
			}
			
			con.close();
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	private void excluir() {
		int confirmaExcluir = JOptionPane.showConfirmDialog(null, "Confirma a exclusão do QRcode ?","Atenção",JOptionPane.YES_NO_OPTION);
		if(confirmaExcluir == JOptionPane.YES_OPTION) {
			String delete = " delete from dadosusuarios where id=?";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(delete);
				pst.setString(1, txtID.getText());
				int confirma = pst.executeUpdate();
				if(confirma == 1) {
					limparCampos();
					JOptionPane.showMessageDialog(null, "QRcode excluido.");
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	private void salvarQRCode() {
	    if (lblFoto.getIcon() == null) {
	        JOptionPane.showMessageDialog(null, "Nenhum QR Code gerado para salvar.");
	        return;
	    }

	    try {
	        BufferedImage qrCodeImage = new BufferedImage(lblFoto.getWidth(), lblFoto.getHeight(), BufferedImage.TYPE_INT_RGB);
	        lblFoto.paint(qrCodeImage.getGraphics());

	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setDialogTitle("Salvar QR Code como imagem");
	        fileChooser.setSelectedFile(new File("QRCode.png"));

	        int userSelection = fileChooser.showSaveDialog(this);
	        if (userSelection == JFileChooser.APPROVE_OPTION) {
	            File fileToSave = fileChooser.getSelectedFile();
	            ImageIO.write(qrCodeImage, "PNG", fileToSave);
	            JOptionPane.showMessageDialog(null, "QR Code salvo com sucesso como imagem.");
	        }
	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(null, "Erro ao salvar o QR Code como imagem: " + e.getMessage());
	    }
	}
	
	private void limparCampos() {
		txtID.setText(null);
		txtNome.setText(null);
		txtNumero.setText(null);
		txtMensagem.setText(null);
		lblFoto.setIcon(null);
		txtNome.requestFocus();
	}
}
