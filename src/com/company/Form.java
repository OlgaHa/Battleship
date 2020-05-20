package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Form extends JFrame {
    private static final int GAME_WIDTH = 1120;
    public static final int FIELD_COUNT = 10;
    public List<JButton> buttonlist = new ArrayList<>();

    public int shipSize;

    JPanel mainPanel;
    JPanel buttonPanel;
    JPanel playerBoard;
    JPanel shipSelectionPanel;

    JPanel myFieldPanel;
    JPanel opponentsFieldPanel;

    JButton btnNew;
    JButton btnReady;


    JLabel lblActivePlayer;

    JButton[][] myFieldButtons = new JButton[FIELD_COUNT][FIELD_COUNT];
    JButton[][] opponentsFieldButtons = new JButton[FIELD_COUNT][FIELD_COUNT];


    JRadioButton oneShip;
    JRadioButton twoShip;
    JRadioButton threeShip;
    JRadioButton fourShip;

    ButtonGroup bgroup;


    private Game game = new Game();

    private int pressedButtons = 0;
    private int shipCount;
    private int count = 0;


    public static final int ELEMENT_SIZE = 50;

    public Form() {
        super("Kartupelis");
        this.setSize(GAME_WIDTH, 700);
        this.setResizable(false);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        initializeButtonPanel();
        initializeFieldPanel();
        initializeShipPanel();


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.ipady = 50;


        mainPanel.add(buttonPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.ipady = 550;

        mainPanel.add(playerBoard, c);

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.ipady = 100;

        mainPanel.add(shipSelectionPanel, c);

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void initializeShipPanel() {
        shipSelectionPanel = new JPanel();
        shipSelectionPanel.setBackground(Color.LIGHT_GRAY);
        shipSelectionPanel.setLayout(new GridLayout(1, 5));

        btnReady = new JButton();
        btnReady.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.isPlayer1Active()) {
                    game.changeActivePlayer();
                    count = 0;
                    oneShip.setSelected(true);
                } else {
                    game.startGame();
                    setFieldButtonState(false);
                }

                updateReadyButtonState();
                redrawFields();
            }
        });


        oneShip = new JRadioButton("One deck ships (place 4)");
        oneShip.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    shipSize = 1;
                    shipCount = 4;
                    count = 0;
                }
            }
        });


        twoShip = new JRadioButton("Two deck ships (place 3");
        twoShip.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    shipSize = 2;
                    shipCount = 3;
                    count = 0;
                }
            }
        });

        threeShip = new JRadioButton("Three deck ships (place 2");
        threeShip.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    shipSize = 3;
                    shipCount = 2;
                    count = 0;
                }
            }
        });
        fourShip = new JRadioButton("Four deck ship (place 1)");
        fourShip.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    shipSize = 4;
                    shipCount = 1;
                    count = 0;
                }
            }
        });


        bgroup = new ButtonGroup();
        bgroup.add(oneShip);
        bgroup.add(twoShip);
        bgroup.add(threeShip);
        bgroup.add(fourShip);


        shipSelectionPanel.add(oneShip);
        shipSelectionPanel.add(twoShip);
        shipSelectionPanel.add(threeShip);
        shipSelectionPanel.add(fourShip);

        btnReady.setText("Continue");
        shipSelectionPanel.add(btnReady);


        btnReady.setEnabled(false);
        oneShip.setSelected(true);

    }

    private void initializeFieldPanel() {
        playerBoard = new JPanel();
        playerBoard.setBackground(Color.LIGHT_GRAY);
        playerBoard.setLayout(new GridLayout(1, 2));
        myFieldPanel = new JPanel();
        myFieldPanel.setBackground(Color.WHITE);

        opponentsFieldPanel = new JPanel();

        var board = game.getBoard();

        addHeaders(myFieldPanel, board);
        addHeaders(opponentsFieldPanel, board);

        addFields(myFieldPanel, myFieldButtons, true);
        addFields(opponentsFieldPanel, opponentsFieldButtons, false);

        playerBoard.add(opponentsFieldPanel);
        playerBoard.add(myFieldPanel);

    }


    private void initializeButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.setSize(GAME_WIDTH, 50);
        btnNew = new JButton("New game");
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                var result = JOptionPane.showConfirmDialog(null, "Do you want to restart the game?");

                if (result == 0) {
                    game.restart();
                    redrawFields();
                    setFieldButtonState(true);
                }
            }
        });


        lblActivePlayer = new JLabel();

        setPlayerLabelText();

        buttonPanel.add(btnNew);
        buttonPanel.add(lblActivePlayer);


    }

    private void setPlayerLabelText() {
        if (game.isPlayer1Active()) {
            lblActivePlayer.setText("  Player 1");
        } else {
            lblActivePlayer.setText("  Player 2");
        }
    }

    private void addHeaders(JPanel panel, Board board) {
        panel.setLayout(null);
        int currentPos = 50;
        for (int j = 0; j < 10; j++) {
            JLabel label = new JLabel(Field.X_COORD_LETTERS.substring(j, j + 1), SwingConstants.CENTER);
            label.setBounds(currentPos, 0, ELEMENT_SIZE, ELEMENT_SIZE);
            panel.add(label);
            currentPos += ELEMENT_SIZE;
        }

        currentPos = 50;
        for (int i = 1; i <= 10; i++) {
            JLabel label = new JLabel(Integer.toString(i), SwingConstants.CENTER);
            label.setBounds(0, currentPos, ELEMENT_SIZE, ELEMENT_SIZE);
            panel.add(label);
            currentPos += ELEMENT_SIZE;
        }

    }


//////////////////////

    private void addFields(JPanel myFieldPanel, JButton[][] fieldButtons, boolean isMyField) {
        String s = "KARTUPELIS";
        int yPos = ELEMENT_SIZE;
        for (int i = 0; i < FIELD_COUNT; i++) {
            int xPos = ELEMENT_SIZE;
            for (int j = 0; j < FIELD_COUNT; j++) {
                JButton btn = new JButton();
                btn.setName(s.substring(i, i + 1) + "" + j + "_" + i + j);
                btn.setBounds(xPos, yPos, ELEMENT_SIZE, ELEMENT_SIZE);
                btn.setBackground(Color.WHITE);
                btn.setEnabled(isMyField);


                btn.addActionListener(actionListener -> {
                    if (!game.hasPlayer1PlacedAllShips() && !game.hasPlayer2PlacedAllShips()) {
                        selectShips(btn);
                        updateReadyButtonState();
                    } else {
                        shootField(btn);
                        updateReadyButtonState();
                    }

                });
                fieldButtons[i][j] = btn;
                myFieldPanel.add(btn);
                xPos += ELEMENT_SIZE;
            }
            yPos += ELEMENT_SIZE;
        }
    }

    private void selectShips(JButton btn) {
        List<int[]> buttonNames = new ArrayList<>();
        if (count < shipCount) {
            var board = game.getBoard();
            var myField = board.getMyField();

            pressedButtons++;
            buttonlist.add(btn);
            if (pressedButtons == shipSize) {
                for (JButton jb : buttonlist) {

                    buttonNames.add(Field.parseCoordinates(jb.getName()));
                }
                if (myField.checkForBuffer(buttonNames) &&
                        myField.areCoordinatesConnected(buttonNames) &&
                        myField.areCoordinatesDifferent(buttonNames)) {
                    myField.placeAShip(buttonNames);
                } else {
                    count = count - 1;
                }
                count++;
                redrawMyField();
                buttonlist.clear();
                buttonNames.clear();
                pressedButtons = 0;
            }
        }
    }

    private void redrawMyField() {
        var board = game.getBoard();
        var myField = board.getMyField();
        for (int i = 0; i < FIELD_COUNT; i++) {
            for (int j = 0; j < FIELD_COUNT; j++) {
                ShipState state = myField.getValueAt(i, j);
                if (ShipState.EMPTY.equals(state)) {
                    myFieldButtons[i][j].setBackground(Color.WHITE);
                } else if (ShipState.MARKED.equals(state)) {
                    myFieldButtons[i][j].setBackground(Color.GREEN);
                } else if (ShipState.SHOT.equals(state)) {
                    myFieldButtons[i][j].setBackground(Color.ORANGE);
                } else if (ShipState.DROWN.equals(state)) {
                    myFieldButtons[i][j].setBackground(Color.RED);
                } else if (ShipState.MISSED.equals(state)) {
                    myFieldButtons[i][j].setBackground(Color.GRAY);
                }
            }
        }
        setPlayerLabelText();
    }

    private void shootField(JButton btn) {
        if (game.canSwitchActivePlayer()) {
            return;
        }

        var activeBoard = game.getBoard();

        var opponentField = activeBoard.getOpponentField();
        var myField = activeBoard.getMyField();

        int[] coordinates = Field.parseCoordinates(btn.getName());
        ShipState result = opponentField.shootShip(coordinates);

        if (ShipState.MISSED.equals(result)) {
            game.setCanSwitchActivePlayer(true);
        }

        redrawFields();

        if (opponentField.getShipDrowned() == opponentField.getShipCount()) {
            String message = "";
            if (game.isPlayer1Active()) {
                message = "Player 1 has won";
            } else {
                message = "Player 2 has won";
            }
            JOptionPane.showMessageDialog(null, message);
            gameHasFinished();
        }
        updateReadyButtonState();
    }


    private void redrawOponentsField() {
        var activeBoard = game.getBoard();
        var opponentField = activeBoard.getOpponentField();

        for (int i = 0; i < FIELD_COUNT; i++) {
            for (int j = 0; j < FIELD_COUNT; j++) {
                ShipState state = opponentField.getValueAt(i, j);
                if (ShipState.EMPTY.equals(state)) {
                    opponentsFieldButtons[i][j].setBackground(Color.WHITE);
                } else if (ShipState.MARKED.equals(state)) {
                    opponentsFieldButtons[i][j].setBackground(Color.WHITE);
                } else if (ShipState.MISSED.equals(state)) {
                    opponentsFieldButtons[i][j].setBackground(Color.LIGHT_GRAY);
                } else if (ShipState.SHOT.equals(state)) {
                    opponentsFieldButtons[i][j].setBackground(Color.ORANGE);
                } else if (ShipState.DROWN.equals(state)) {
                    opponentsFieldButtons[i][j].setBackground(Color.RED);
                }
            }
        }
    }


    private void updateReadyButtonState() {
        if (game.isPlayer1Active()) {
            btnReady.setEnabled(game.hasPlayer1PlacedAllShips());
            btnReady.setVisible(game.hasPlayer1PlacedAllShips());
        } else {
            btnReady.setText("Start game");
            btnReady.setEnabled(game.hasPlayer2PlacedAllShips());
            btnReady.setVisible(game.hasPlayer2PlacedAllShips());
        }

        if (game.isGameStarted()) {
            btnReady.setText("Switch");
            btnReady.setEnabled(game.canSwitchActivePlayer());
            btnReady.setVisible(game.canSwitchActivePlayer());

        }
    }

    private void redrawFields() {
        redrawMyField();
        redrawOponentsField();
    }


    private void setFieldButtonState(boolean myFieldsEnabled) {
        for (int i = 0; i < FIELD_COUNT; i++) {
            for (int j = 0; j < FIELD_COUNT; j++) {
                myFieldButtons[i][j].setEnabled(myFieldsEnabled);
                opponentsFieldButtons[i][j].setEnabled(!myFieldsEnabled);
            }
        }
    }

    private void gameHasFinished() {
        btnReady.setVisible(false);
        for (int i = 0; i < FIELD_COUNT; i++) {
            for (int j = 0; j < FIELD_COUNT; j++) {
                opponentsFieldButtons[j][i].setEnabled(false);
            }
        }
    }

}
