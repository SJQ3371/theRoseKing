package client;

import commons.api.GameApi;
import commons.api.InitApi;
import commons.model.PowerCard;
import client.pane.GameBoard;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Game extends JFrame {

    GameApi gameFn;
    String color;
    JPanel buttonPanel;
    GameBoard gameBoard;
    String playColor;
    JLabel headerLabel;

    public Game(GameApi api) throws RemoteException {
        this.gameFn = api;
        this.color = api.getColor(UUID.randomUUID().toString());
        this.setTitle("The Rose King");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        initGrid();
        // A thread helper page is displayed
        new Thread(() -> {
            while (true)
                try {
                    gameBoard.repaint();
                    if (api.isGameOver()) {
                        int redScore = api.getScore("RED");
                        int whiteScore = api.getScore("WHITE");
                        String winColor = "";
                        if (redScore == whiteScore) {
                            int whiteCoinCount = api.getCoinCount("WHITE");
                            int redCoinCount = api.getCoinCount("RED");
                            if (whiteCoinCount > redCoinCount) winColor = "WHITE";
                            else winColor = "RED";
                        } else {
                            if (redScore > whiteScore) winColor = "RED";
                            else winColor = "WHITE";
                        }
                        headerLabel.setText("Win:" + winColor + " White Score:" + whiteScore + " Red Score:" + redScore);
                        buttonPanel.removeAll();
                        buttonPanel.add(new JLabel("Game Over!"));
                        buttonPanel.updateUI();
                        this.validate();
                    } else {
                        int redScore = api.getScore("RED");
                        int whiteScore = api.getScore("WHITE");
                        headerLabel.setText("Red Score:" + redScore + " White Score:" + whiteScore + " Player：" + api.getPlayColor() + " Your：" + color);
                        if (!api.getPlayColor().equals(playColor)) {
                            playColor = api.getPlayColor();
                            this.refreshBottomButtons();
                        }
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }).start();
        this.setVisible(true);
        this.setResizable(false);
        this.setSize(460, 800);
        this.setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    api.closeLink();
                    System.exit(0);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    void refreshBottomButtons() throws RemoteException {
        List<JButton> buttonList = new ArrayList<>();
        buttonPanel.removeAll();

        final JCheckBox checkBox = new JCheckBox("Hero Cards (" + gameFn.getHeroCount(color) + ")");
        if (gameFn.getHeroCount(color) > 0) {
            buttonPanel.add(checkBox);
        }
        Map<PowerCard, Integer> powerCard = gameFn.getPowerCard(color);
        for (PowerCard card : powerCard.keySet()) {
            Integer count = powerCard.get(card);
            String title = card.getLabel();
            JButton btn = new JButton(title);
            btn.addActionListener(e -> {
                try {
                    boolean isHero = checkBox.isSelected();
                    if (gameFn.playCard(color, card, isHero)) {
                        gameBoard.repaint();
                        gameFn.swap();
                        refreshBottomButtons();
                    } else {
                        JOptionPane.showMessageDialog(null, "Move failure");
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            });
            buttonList.add(btn);
        }

        JButton drawCard = new JButton("Draw card");
        drawCard.addActionListener(e -> {
            try {
                if (powerCard.size() < 5) {
                    gameFn.drawCard(color);
                    gameFn.swap();
                } else {
                    JOptionPane.showMessageDialog(null, "You already have five cards");
                }
                refreshBottomButtons();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
        buttonList.add(drawCard);
        if (!gameFn.getPlayColor().equals(color)) {
            checkBox.setEnabled(false);
            for (JButton button : buttonList) {
                button.setEnabled(false);
            }
        }

        for (JButton jButton : buttonList) {
            buttonPanel.add(jButton);
        }


        buttonPanel.updateUI();
        this.validate();
    }

    void initGrid() throws RemoteException {
        headerLabel = new JLabel();
        headerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // 创建下方按钮行
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 3, 5, 5));

        refreshBottomButtons();
        gameBoard = new GameBoard(gameFn);
        this.add(headerLabel, BorderLayout.NORTH);
        this.add(gameBoard, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                InitApi initApi = (InitApi) Naming.lookup("rmi://localhost:1099/api");
                String name = JOptionPane.showInputDialog("Please enter the game room:");
                initApi.createGame(name);
                GameApi api = (GameApi) Naming.lookup("rmi://localhost:1099/" + name);
                Game game = new Game(api);
                game.setVisible(true);
            } catch (RemoteException | NotBoundException | MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }
}
