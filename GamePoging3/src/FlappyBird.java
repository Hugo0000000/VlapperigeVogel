import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
    Image betweenPipeImg;

    // Bird class
    int birdX = boardWidth / 12;
    int birdY = boardWidth / 4;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // Pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64; // scaled by 1/6
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;
        boolean isVisible = true;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // Game logic
    Bird bird;
    double velocityX = -4; // move pipes to the left speed (simulates bird moving right)
    double velocityY = 0; // move bird up/down speed
    double gravity = 0.5;

    ArrayList<Pipe> pipes;
    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;


    int currentPipesRows = -1;
    int attempts = 1;
    boolean isCompleted = false;

    // Question system
    String[] questions = {
            "Wat is 2 + 2?",
            "Wat is de hoofdstad van Frankrijk?",
            "Wat is 5 * 3?",
            "Wat is de grootste zee in de wereld?",
            "Wat eet een koe?",
            "Wat is de grootste planeet?",
            "Hoe noem je de baby van een hond?",
            "De hond ... in de tuin?",
            "Hoveel dagen zitten er in een week?",
            "Hoe heet het seizoen waarin de baderen van de bomen vallen?"
    };
    String[][] answers = {
            {"3", "4", "5"},
            {"Berlijn", "Madrid", "Parijs"},
            {"15", "10", "20"},
            {"Noordzee", "De Rijn", "Stille Oceaan"},
            {"Gras", "Varken", "Koeien"},
            {"Mars", "Jupiter", "Zon"},
            {"Kalf", "Kleine hond", "Puppy"},
            {"Loopt", "Loop", "Loopdt"},
            {"6", "7", "9"},
            {"Winter", "Zomer", "Herfst"}
    };
    int[] correctAnswers = {1, 2, 0, 2, 0, 1, 2, 0, 1, 2}; // Indexes of correct answers in each question
    int currentQuestionIndex = 0;
    int maxPipesRows = questions.length;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        betweenPipeImg = new ImageIcon(getClass().getResource("./betweenpipe.png")).getImage();

        // Bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<>();

        // Place pipes timer
        placePipeTimer = new Timer(4000, e -> placePipes());
        placePipeTimer.start();

        // Game timer
        gameLoop = new Timer(1000 / 60, this); // how long it takes to start timer, milliseconds gone between frames
        gameLoop.start();
    }

    void placePipes() {
        if (currentPipesRows >= maxPipesRows) {
            placePipeTimer.stop();
            isCompleted = true;
            return;
        } else {
            currentPipesRows++;
        }

        // Top pipe
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = -480;
        pipes.add(topPipe);

        // Middle pipes
        int[] middlePipeYPositions = {145, 380}; // Specify the exact y-positions of the middle pipes
        int middlePipeHeight = pipeHeight / 4; // Define height of the middle pipes

        for (int yPosition : middlePipeYPositions) {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = yPosition;
            betweenPipe.height = middlePipeHeight;
            betweenPipe.width = pipeWidth; // Adjust width if needed
            pipes.add(betweenPipe);
        }
        int correctAnswerIndex = correctAnswers[(int) score]; // Get the correct answer for the current question

        // Set booleans for answers
        boolean antwoord1 = correctAnswerIndex == 0;
        boolean antwoord2 = correctAnswerIndex == 1;
        boolean antwoord3 = correctAnswerIndex == 2;

        if (antwoord1 == false) {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = 33;
            betweenPipe.height = middlePipeHeight;
            betweenPipe.width = pipeWidth; // Adjust width if needed
            betweenPipe.isVisible = false;
            pipes.add(betweenPipe);
        }
        else {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = 700;
        }
        if (antwoord2 == false) {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = 150 + 128;
            betweenPipe.height = middlePipeHeight;
            betweenPipe.width = pipeWidth; // Adjust width if needed
            betweenPipe.isVisible = false;
            pipes.add(betweenPipe);
        }
        else {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = 700;
        }
        if (antwoord3 == false) {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = 380 + 128;
            betweenPipe.height = middlePipeHeight;
            betweenPipe.width = pipeWidth; // Adjust width if needed
            betweenPipe.isVisible = false;
            pipes.add(betweenPipe);
        }
        else {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = 700;
        }

        // Bottom pipe
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = 630;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        // Bird
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        // Pipes
        for (Pipe pipe : pipes) {
            if (pipe.isVisible) {
                g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
            }
        }

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString("Score: " + (int) score, 10, 35);

        // Question and answers
        if (!gameOver) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Question: " + questions[currentQuestionIndex], 10, 100);
            for (int i = 0; i < answers[currentQuestionIndex].length; i++) {
                g.setFont(new Font("Arial", Font.PLAIN, 18));
                g.drawString((i+ 1) + ". " + answers[currentQuestionIndex][i], 10, 130 + i * 220);
            }
        }

        // Game over messages
        if (gameOver) {
            g.setFont(new Font("Arial", Font.PLAIN, 50));
            g.drawString("Game Over", 80, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("Attempts: " + attempts, 10, 360);
        }
    }

    public void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.25;
                pipe.passed = true;

                // Update question index based on the integer part of the score
                currentQuestionIndex = Math.min((int) score, questions.length - 1);
            }

            if (collision(bird, pipe)) {
                pipes.clear();
                gameOver = true;
                return;
            }
        }

        if (score >= maxPipesRows) {
            gameOver = true;
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && // a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x && // a's top right corner passes b's top left corner
                a.y < b.y + b.height && // a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y; // a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) { // called every x milliseconds by gameLoop timer
        if (!gameOver) {
            move();
            repaint();
        } else {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (isCompleted) {
                return; // Prevent restarting the game after completing all pipes
            }

            velocityY = -6; // Make the bird jump

            if (gameOver) {
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                currentPipesRows = -1;
                attempts++;
                currentQuestionIndex = 0;

                gameLoop.start();
                placePipeTimer.start();
            }
        } else if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_3) {
            int answerIndex = e.getKeyCode() - KeyEvent.VK_1;
            if (answerIndex == correctAnswers[currentQuestionIndex]) {
                score++;
                currentQuestionIndex = Math.min((int) score, questions.length - 1); // Move to the next question
            } else {
                gameOver = true; // End the game if the answer is wrong
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
