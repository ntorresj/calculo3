package calculo3d;

import com.sun.opengl.util.Animator;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Calculus.java <BR>
 * author: Nicolas Ignacio Torres Jara
 */
public class Calculus implements GLEventListener, MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {

    float X = 0;
    int mod = 6;
    float M[][] = new float[2000][2000];
    float Y = 0;
    double S = .5, xClick, yClick, mov = 0;
    static boolean pressCtrl = false, stop = false, pressShift = false;
    private static boolean intersectar = false;
    private static double a, b, c, d;
    private static double ex, ey, ez;
    private static double xFacT, xAuxT, yFacT, yAuxT, zFacT, zAuxT;
    private static String xTrigoT, yTrigoT, zTrigoT;
    static ArrayList<Float> mx = new ArrayList<Float>();
    static ArrayList<Float> my = new ArrayList<Float>();
    static ArrayList<Float> mz = new ArrayList<Float>();
    static ArrayList<Float> px = new ArrayList<Float>();
    static ArrayList<Float> py = new ArrayList<Float>();
    static ArrayList<Float> pz = new ArrayList<Float>();

    public static void main(String[] args) {
        JFrame jframe = new JFrame("Graficador de Superficies");
        jframe.setLayout(new BorderLayout());
        GLCanvas canvas = new GLCanvas();
        JPanel barraSuper = new JPanel(new FlowLayout());
        JPanel barraAbj = new JPanel(new FlowLayout());
        JButton plot = new JButton("Plotear");
        setIntersectar(false);
        final JCheckBox inter = new JCheckBox("Intersectar", isIntersectar());
        JButton limpiar = new JButton("Limpiar");
        JButton plotPara = new JButton("Plotear");
        final JTextField ec = new JTextField();
        final JTextField x = new JTextField();
        final JTextField y = new JTextField();
        final JTextField z = new JTextField();
        JLabel nomF2 = new JLabel("Superficies     ");
        JLabel nomF1 = new JLabel("Ec. Parametrica     ");
        JLabel lX = new JLabel("X: ");
        JLabel lY = new JLabel("Y: ");
        JLabel lZ = new JLabel("Z: ");
        ec.setColumns(20);
        x.setColumns(5);
        y.setColumns(5);
        z.setColumns(5);
        ec.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if ((e.getKeyChar() == ' ') || (e.getKeyChar() != 'x') && (e.getKeyChar() != 'y')
                        && (e.getKeyChar() != 'z') && (e.getKeyChar() != '-') && (e.getKeyChar() != '+')
                        && (e.getKeyChar() != '^') && !(Character.isDigit(e.getKeyChar()))) {
                    e.consume();
                }
            }
        });
        x.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if ((e.getKeyChar() == ' ') || (e.getKeyChar() != 't') && (e.getKeyChar() != 'c') && (e.getKeyChar() != 'o') && (e.getKeyChar() != 's')
                        && (e.getKeyChar() != '-') && (e.getKeyChar() != '+') && (e.getKeyChar() != 'n') && (e.getKeyChar() != 'e') && (e.getKeyChar() != 'a')
                        && (e.getKeyChar() != '^') && (e.getKeyChar() != '(') && (e.getKeyChar() != ')') && !(Character.isDigit(e.getKeyChar())) && (e.getKeyChar() != '.')) {
                    e.consume();
                }
            }
        });
        y.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if ((e.getKeyChar() == ' ') || (e.getKeyChar() != 't') && (e.getKeyChar() != 'c') && (e.getKeyChar() != 'o') && (e.getKeyChar() != 's')
                        && (e.getKeyChar() != '-') && (e.getKeyChar() != '+') && (e.getKeyChar() != 'n') && (e.getKeyChar() != 'e') && (e.getKeyChar() != 'a')
                        && (e.getKeyChar() != '^') && (e.getKeyChar() != '(') && (e.getKeyChar() != ')') && !(Character.isDigit(e.getKeyChar())) && (e.getKeyChar() != '.')) {
                    e.consume();
                }
            }
        });
        z.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if ((e.getKeyChar() == ' ') || (e.getKeyChar() != 't') && (e.getKeyChar() != 'c') && (e.getKeyChar() != 'o') && (e.getKeyChar() != 's')
                        && (e.getKeyChar() != '-') && (e.getKeyChar() != '+') && (e.getKeyChar() != 'n') && (e.getKeyChar() != 'e') && (e.getKeyChar() != 'a')
                        && (e.getKeyChar() != '^') && (e.getKeyChar() != '(') && (e.getKeyChar() != ')') && !(Character.isDigit(e.getKeyChar())) && (e.getKeyChar() != '.')) {
                    e.consume();
                }
            }
        });
        plot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (ec.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Ingrese Ecuacion", "Error!", 0);
                    } else {
                        encontrarValores(ec.getText());
                        if (!isIntersectar()) {
                            px.removeAll(px);
                            py.removeAll(py);
                            pz.removeAll(pz);
                            mx.removeAll(mx);
                            my.removeAll(my);
                            mz.removeAll(mz);
                        }
                        if (((getEx() == 1) && (getEy() == 1) && (getEz() == 1)) || ((getEx() == 2) && (getEy() == 2) && (getEz() == 2) && (getD() == 0))) {
                            puntosPlano(getA(), getB(), getC(), getD(), getEx(), getEy(), getEz());
                        } else {
                            puntos(getA(), getB(), getC(), getD(), getEx(), getEy(), getEz());
                        }
                    }
                } catch (Exception ex) {
                }
            }
        });
        plotPara.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if ((x.getText().equals("")) || (y.getText().equals("")) || (z.getText().equals(""))) {
                        JOptionPane.showMessageDialog(null, "Ingrese Valores", "Error!", 0);
                    } else {
                        if (!isIntersectar()) {
                            mx.removeAll(mx);
                            my.removeAll(my);
                            mz.removeAll(mz);
                            px.removeAll(px);
                            py.removeAll(py);
                            pz.removeAll(pz);
                        }
                        String ecX = trigonometriaX(x.getText());
                        String ecY = trigonometriaY(y.getText());
                        String ecZ = trigonometriaZ(z.getText());
                        if ((!getxTrigoT().equals("")) || (!getyTrigoT().equals("")) || (!getzTrigoT().equals(""))) {
                            parametricaTri(ecX, ecY, ecZ);
                        } else {
                            valoresParametricaX(x.getText());
                            valoresParametricaY(y.getText());
                            valoresParametricaZ(z.getText());
                            parametricaNormal(getxFacT(), getxAuxT(), getyFacT(), getyAuxT(), getzFacT(), getzAuxT());
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ecuacion mal ingresada", "Error!", 0);
                }
            }
        });
        inter.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                System.out.println(e.getStateChange());
                if (e.getStateChange() == 1) {
                    setIntersectar(true);
                } else if (e.getStateChange() == 2) {
                    setIntersectar(false);
                }
            }
        });
        limpiar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                px.removeAll(px);
                py.removeAll(py);
                pz.removeAll(pz);
                mx.removeAll(mx);
                my.removeAll(my);
                mz.removeAll(mz);
            }
        });
        barraSuper.add(nomF2);
        barraSuper.add(ec);
        barraSuper.add(plot);
        barraSuper.add(limpiar);
        barraSuper.add(inter);
        jframe.getContentPane().add(barraSuper, BorderLayout.NORTH);
        barraAbj.add(nomF1);
        barraAbj.add(lX);
        barraAbj.add(x);
        barraAbj.add(lY);
        barraAbj.add(y);
        barraAbj.add(lZ);
        barraAbj.add(z);
        barraAbj.add(plotPara);
        jframe.getContentPane().add(barraAbj, BorderLayout.SOUTH);
        canvas.addGLEventListener(new Calculus());
        jframe.add(canvas);
        jframe.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        jframe.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        gl.setSwapInterval(1);

        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH);
        drawable.addMouseMotionListener(this);
        drawable.addMouseListener(this);
        drawable.addMouseWheelListener(this);
        drawable.addKeyListener(this);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) {
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        //glu.gluLookAt(5.0, 5.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        gl.glTranslatef(0.0f, 0.0f, -15.0f);
        gl.glRotated(Y, 1, 0, 0);
        gl.glRotated(X, 0, 1, 0);
        gl.glRotated(yClick += mov, 0, 1, 0);
        gl.glRotated(xClick += mov, 1, 0, 0);
        gl.glBegin(gl.GL_LINES);
        gl.glColor3d(1, 0, 0); // Rojo, X
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(2, 0, 0);
        gl.glColor3d(0, 1, 0); // Verde, Y
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(0, 2, 0);
        gl.glColor3d(0, 0, 1); // Azul, Z
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(0, 0, 2);
        gl.glEnd();
        for (int i = -0; i < 20; i++) {
            M[i][0] = (float)(Math.random()*200);
        }
        for (int j = 0; j < 20; j++) {
            M[0][j] = (float)(Math.random()*200);
        }
//        intentoDeRender(drawable);
        superficie(drawable);
        superficiePara(drawable);
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void superficie(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glScaled(S, S, S);
        gl.glBegin(gl.GL_POINTS);
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        for (int i = 0; i < mx.size(); i++) {
            gl.glVertex3f(mx.get(i), my.get(i), mz.get(i));
        }
        gl.glEnd();
    }

    public void intentoDeRender(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        float[] rgba = {1f, 1f, 1f};
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 0.5f);
        //gl.glEnable(GL.GL_TEXTURE_2D);
        //gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
        gl.glBegin(GL.GL_TRIANGLE_STRIP);
        for (int i = 0; i < 20; i += mod) {

            if ((i / mod) % 2 == 0) {
//                setColor(gl,M[i][0]);
//                gl.glTexCoord2d(i / 1201.0, 0);
                gl.glVertex3f(i, M[i][0], 0);
                for (int j = 0; j < 20; j += mod) {
//                setColor(gl,M[i+mod][j]);
//                    gl.glTexCoord2d((i + mod) / 1201.0, j / 1201.0);
                    gl.glVertex3f(i + mod, M[i + mod][j], j);
                    //gl.glNormal3f(i+mod,M[i+mod][j],j);
//                setColor(gl,M[i][j]);
//                    gl.glTexCoord2d(i / 1201.0, j / 1201.0);
                    gl.glVertex3f(i, M[i][j], j);
                    //gl.glNormal3f(i,M[i][j],j);
                }
            } else {
//                setColor(gl,M[i+mod][1200]);
//                gl.glTexCoord2d((i + mod) / 1201.0, 1.0);
                gl.glVertex3f(i + mod, M[i + mod][20], 20);
                for (int j = 20 - 20 % mod; j >= 0; j -= mod) {
//                setColor(gl,M[i+mod][j]);
//                    gl.glTexCoord2d((i + mod) / 1201.0, j / 1201.0);
                    gl.glVertex3f(i + mod, M[i + mod][j], j);
                    //gl.glNormal3f(i+mod,M[i+mod][j],j);
//                setColor(gl,M[i][j]);
//                    gl.glTexCoord2d(i / 1201.0, j / 1201.0);
                    gl.glVertex3f(i, M[i][j], j);
                    //gl.glNormal3f(i,M[i][j],j);
                }
            }
        }
        gl.glEnd();
    }

    public static void puntos(double a, double b, double c, double d, double ex, double ey, double ez) {
        for (float x = -10; x < 11; x += .5) {// += .5
            for (float y = -10; y < 11; y += .5) {
                for (float z = -10; z < 11; z += .5) {
                    if (((a * (float) ((int) ((10 * (Math.pow(x, ex)))) / 10)) + (b * (float) ((int) ((10 * (Math.pow(y, ey)))) / 10)) + (c * (float) ((int) ((10 * (Math.pow(z, ez)))) / 10))) == d) {
                        mx.add(x);
                        my.add(y);
                        mz.add(z);
                    }
                }
            }
        }
    }

    public static void puntosPlano(double a, double b, double c, double d, double ex, double ey, double ez) {
        for (float x = -10; x < 11; x++) {// += .5
            for (float y = -10; y < 11; y++) {
                for (float z = -10; z < 11; z++) {
                    if (((a * (float) ((int) ((10 * (Math.pow(x, ex)))) / 10)) + (b * (float) ((int) ((10 * (Math.pow(y, ey)))) / 10)) + (c * (float) ((int) ((10 * (Math.pow(z, ez)))) / 10))) == d) {
                        mx.add(x);
                        my.add(y);
                        mz.add(z);
                    }
                }
            }
        }
    }

    public static String trigonometriaX(String eq) {
        int posDI = 0, posDF = 0;
        String ec = "";
        boolean entro = true, ajuste = false;
        for (int i = 0; entro && i < eq.length() - 4; i++) {
            if ((eq.charAt(i) == 's') || (eq.charAt(i) == 'c') || (eq.charAt(i) == 't')) {
                ajuste = true;
                posDI = i;
                posDF = posDI;
                for (int j = 0; entro; j++) {
                    if ((eq.charAt(j) == 's') && (eq.charAt(j + 3) == '(')) {
                        entro = false;
                        for (int k = j; eq.charAt(Math.abs(k - 1)) != ')'; k++) {
                            ec += eq.charAt(k);
                            posDF++;
                        }
                    } else if ((eq.charAt(j) == 'c') && (eq.charAt(j + 3) == '(')) {
                        entro = false;
                        for (int k = j; eq.charAt(Math.abs(k - 1)) != ')'; k++) {
                            ec += eq.charAt(k);
                            posDF++;
                        }
                    } else if ((eq.charAt(j) == 't') && (eq.charAt(j + 3) == '(')) {
                        entro = false;
                        for (int k = j; eq.charAt(Math.abs(k - 1)) != ')'; k++) {
                            ec += eq.charAt(k);
                            posDF++;
                        }
                    }
                }
            }
        }
        if (ajuste) {
            if ((Character.isDigit(eq.charAt(0))) || (eq.charAt(0) == '-')) {
                eq = eq.substring(0, posDI - 1);
            } else if ((eq.length() > posDF) && (eq.charAt(posDF)) == '+') {
                eq = eq.substring(posDF + 1, eq.length());
            } else {
                eq = eq.substring(posDF, eq.length());
            }
        }
        setxTrigoT(ec);
        return eq;
    }

    public static String trigonometriaY(String eq) {
        int posDI = 0, posDF = 0;
        String ec = "";
        boolean entro = true, ajuste = false;
        for (int i = 0; entro && i < eq.length() - 4; i++) {
            if ((eq.charAt(i) == 's') || (eq.charAt(i) == 'c') || (eq.charAt(i) == 't')) {
                ajuste = true;
                posDI = i;
                posDF = posDI;
                for (int j = 0; entro; j++) {
                    if ((eq.charAt(j) == 's') && (eq.charAt(j + 3) == '(')) {
                        entro = false;
                        for (int k = j; eq.charAt(Math.abs(k - 1)) != ')'; k++) {
                            ec += eq.charAt(k);
                            posDF++;
                        }
                    } else if ((eq.charAt(j) == 'c') && (eq.charAt(j + 3) == '(')) {
                        entro = false;
                        for (int k = j; eq.charAt(Math.abs(k - 1)) != ')'; k++) {
                            ec += eq.charAt(k);
                            posDF++;
                        }
                    } else if ((eq.charAt(j) == 't') && (eq.charAt(j + 3) == '(')) {
                        entro = false;
                        for (int k = j; eq.charAt(Math.abs(k - 1)) != ')'; k++) {
                            ec += eq.charAt(k);
                            posDF++;
                        }
                    }
                }
            }
        }
        if (ajuste) {
            if ((Character.isDigit(eq.charAt(0))) || (eq.charAt(0) == '-')) {
                eq = eq.substring(0, posDI - 1);
            } else if ((eq.length() > posDF) && (eq.charAt(posDF)) == '+') {
                eq = eq.substring(posDF + 1, eq.length());
            } else {
                eq = eq.substring(posDF, eq.length());
            }
        }
        setyTrigoT(ec);
        return eq;
    }

    public static String trigonometriaZ(String eq) {
        int posDI = 0, posDF = 0;
        String ec = "";
        boolean entro = true, ajuste = false;
        for (int i = 0; entro && i < eq.length() - 4; i++) {
            if ((eq.charAt(i) == 's') || (eq.charAt(i) == 'c') || (eq.charAt(i) == 't')) {
                ajuste = true;
                posDI = i;
                posDF = posDI;
                for (int j = 0; entro; j++) {
                    if ((eq.charAt(j) == 's') && (eq.charAt(j + 3) == '(')) {
                        entro = false;
                        for (int k = j; eq.charAt(Math.abs(k - 1)) != ')'; k++) {
                            ec += eq.charAt(k);
                            posDF++;
                        }
                    } else if ((eq.charAt(j) == 'c') && (eq.charAt(j + 3) == '(')) {
                        entro = false;
                        for (int k = j; eq.charAt(Math.abs(k - 1)) != ')'; k++) {
                            ec += eq.charAt(k);
                            posDF++;
                        }
                    } else if ((eq.charAt(j) == 't') && (eq.charAt(j + 3) == '(')) {
                        entro = false;
                        for (int k = j; eq.charAt(Math.abs(k - 1)) != ')'; k++) {
                            ec += eq.charAt(k);
                            posDF++;
                        }
                    }
                }
            }
        }
        if (ajuste) {
            if ((Character.isDigit(eq.charAt(0))) || (eq.charAt(0) == '-')) {
                eq = eq.substring(0, posDI - 1);
            } else if ((eq.length() > posDF) && (eq.charAt(posDF)) == '+') {
                eq = eq.substring(posDF + 1, eq.length());
            } else {
                eq = eq.substring(posDF, eq.length());
            }
        }
        setzTrigoT(ec);
        return eq;
    }

    public void superficiePara(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glScaled(S, S, S);
        gl.glBegin(gl.GL_LINE_STRIP);
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        for (int i = 0; i < px.size(); i++) {
            gl.glVertex3f(px.get(i), py.get(i), pz.get(i));
        }
        gl.glEnd();
    }

    public static void parametricaNormal(double x, double a, double y, double b, double z, double c) {
        for (float t = -10; t < 11; t += .1) {
            px.add((float) (a + x * t));
            py.add((float) (b + y * t));
            pz.add((float) (c + z * t));
        }
    }

    public static void parametricaTri(String x, String y, String z) {
        if (!getxTrigoT().equals("")) {
            if (getxTrigoT().equals("cos(t)")) {
                for (float t = -20; t < 21; t += .1) {
                    px.add((float) (Math.cos(t)));
                }
            } else if (getxTrigoT().equals("sen(t)")) {
                for (float t = -20; t < 21; t += .1) {
                    px.add((float) (Math.sin(t)));
                }
            } else if (getxTrigoT().equals("tan(t)")) {
                for (float t = -20; t < 21; t += .1) {
                    px.add((float) (Math.tan(t)));
                }
            }
        }
        if (!getyTrigoT().equals("")) {
            if (getyTrigoT().equals("cos(t)")) {
                for (float t = -20; t < 21; t += .1) {
                    py.add((float) (Math.cos(t)));
                }
            } else if (getyTrigoT().equals("sen(t)")) {
                for (float t = -20; t < 21; t += .1) {
                    py.add((float) (Math.sin(t)));
                }
            } else if (getyTrigoT().equals("tan(t)")) {
                for (float t = -20; t < 21; t += .1) {
                    py.add((float) (Math.tan(t)));
                }
            }
        }
        if (!getzTrigoT().equals("")) {
            if (getzTrigoT().equals("cos(t)")) {
                for (float t = -20; t < 21; t += .1) {
                    pz.add((float) (Math.cos(t)));
                }
            } else if (getzTrigoT().equals("sen(t)")) {
                for (float t = -20; t < 21; t += .1) {
                    pz.add((float) (Math.sin(t)));
                }
            } else if (getzTrigoT().equals("tan(t)")) {
                for (float t = -20; t < 21; t += .1) {
                    pz.add((float) (Math.tan(t)));
                }
            }
        }
        if ((!getxTrigoT().equals("")) && (getyTrigoT().equals("")) && (getzTrigoT().equals(""))) {
            valoresParametricaY(y);
            valoresParametricaZ(z);
            for (float t = -20; t < 21; t += .1) {
                py.add((float) (getyAuxT() + getyFacT() * t));
                pz.add((float) (getzAuxT() + getzFacT() * t));
            }
        } else if ((getxTrigoT().equals("")) && (!getyTrigoT().equals("")) && (getzTrigoT().equals(""))) {
            valoresParametricaX(x);
            valoresParametricaZ(z);
            for (float t = -20; t < 21; t += .1) {
                px.add((float) (getxAuxT() + getxFacT() * t));
                pz.add((float) (getzAuxT() + getzFacT() * t));
            }
        } else if ((getxTrigoT().equals("")) && (getyTrigoT().equals("")) && (!getzTrigoT().equals(""))) {
            valoresParametricaX(x);
            valoresParametricaY(y);
            for (float t = -20; t < 21; t += .1) {
                px.add((float) (getxAuxT() + getxFacT() * t));
                py.add((float) (getyAuxT() + getyFacT() * t));
            }
        } else if ((!getxTrigoT().equals("")) && (!getyTrigoT().equals("")) && (getzTrigoT().equals(""))) {
            valoresParametricaZ(z);
            for (float t = -20; t < 21; t += .1) {
                pz.add((float) (getzAuxT() + getzFacT() * t));
            }
        } else if ((!getxTrigoT().equals("")) && (getyTrigoT().equals("")) && (!getzTrigoT().equals(""))) {
            valoresParametricaY(y);
            for (float t = -20; t < 21; t += .1) {
                py.add((float) (getyAuxT() + getyFacT() * t));
            }
        } else if ((getxTrigoT().equals("")) && (!getyTrigoT().equals("")) && (!getzTrigoT().equals(""))) {
            valoresParametricaX(x);
            for (float t = -20; t < 21; t += .1) {
                px.add((float) (getxAuxT() + getxFacT() * t));
            }
        }
    }

    public static void valoresParametricaX(String ec) {
        int posDI = 0, posDF = 0;
        boolean t = false;
        String valorT = "", valorAux = "";
        for (int i = 0; i < ec.length(); i++) {
            if (ec.charAt(i) == 't') {
                t = true;
                posDI = i;
                posDF = posDI;
                for (int j = i - 1; j >= 0; j--) {
                    if ((Character.isDigit(ec.charAt(j))) || ec.charAt(j) == '.') {
                        valorT = ec.charAt(j) + valorT;
                        posDF--;
                    } else if (ec.charAt(j) == '-') {
                        valorT = ec.charAt(j) + valorT;
                        posDF--;
                        j = 0;
                    } else if (ec.charAt(j) == '+') {
                        posDF--;
                        j = 0;
                    } else {
                        j = 0;
                    }
                }
                if (valorT.equals("")) {
                    valorT = "1";
                } else if (valorT.equals("-")) {
                    valorT = "-1";
                }
            }
        }
        if (valorT.equals("")) {
            valorT = "0";
        }
        setxFacT(Double.valueOf(valorT));
        if (t) {
            ec = ec.substring(0, posDF) + ec.substring(posDI + 1, ec.length());
        }
        if (ec.length() > 0) {
            if (ec.charAt(0) == '+') {
                ec = ec.substring(1, ec.length());
                valorAux = ec;
            } else {
                valorAux = ec;
            }
        } else {
            valorAux = "0";
        }
        setxAuxT(Double.valueOf(valorAux));
    }

    public static void valoresParametricaY(String ec) {
        int posDI = 0, posDF = 0;
        boolean t = false;
        String valorT = "", valorAux = "";
        for (int i = 0; i < ec.length(); i++) {
            if (ec.charAt(i) == 't') {
                t = true;
                posDI = i;
                posDF = posDI;
                for (int j = i - 1; j >= 0; j--) {
                    if ((Character.isDigit(ec.charAt(j))) || ec.charAt(j) == '.') {
                        valorT = ec.charAt(j) + valorT;
                        posDF--;
                    } else if (ec.charAt(j) == '-') {
                        valorT = ec.charAt(j) + valorT;
                        posDF--;
                        j = 0;
                    } else if (ec.charAt(j) == '+') {
                        posDF--;
                        j = 0;
                    } else {
                        j = 0;
                    }
                }
                if (valorT.equals("")) {
                    valorT = "1";
                } else if (valorT.equals("-")) {
                    valorT = "-1";
                }
            }
        }
        if (valorT.equals("")) {
            valorT = "0";
        }
        setyFacT(Double.valueOf(valorT));
        if (t) {
            ec = ec.substring(0, posDF) + ec.substring(posDI + 1, ec.length());
        }
        if (ec.length() > 0) {
            if (ec.charAt(0) == '+') {
                ec = ec.substring(1, ec.length());
                valorAux = ec;
            } else {
                valorAux = ec;
            }
        } else {
            valorAux = "0";
        }
        setyAuxT(Double.valueOf(valorAux));
    }

    public static void valoresParametricaZ(String ec) {
        int posDI = 0, posDF = 0;
        boolean t = false;
        String valorT = "", valorAux = "";
        for (int i = 0; i < ec.length(); i++) {
            if (ec.charAt(i) == 't') {
                t = true;
                posDI = i;
                posDF = posDI;
                for (int j = i - 1; j >= 0; j--) {
                    if ((Character.isDigit(ec.charAt(j))) || ec.charAt(j) == '.') {
                        valorT = ec.charAt(j) + valorT;
                        posDF--;
                    } else if (ec.charAt(j) == '-') {
                        valorT = ec.charAt(j) + valorT;
                        posDF--;
                        j = 0;
                    } else if (ec.charAt(j) == '+') {
                        posDF--;
                        j = 0;
                    } else {
                        j = 0;
                    }
                }
                if (valorT.equals("")) {
                    valorT = "1";
                } else if (valorT.equals("-")) {
                    valorT = "-1";
                }
            }
        }
        if (valorT.equals("")) {
            valorT = "0";
        }
        setzFacT(Double.valueOf(valorT));
        if (t) {
            ec = ec.substring(0, posDF) + ec.substring(posDI + 1, ec.length());
        }
        if (ec.length() > 0) {
            if (ec.charAt(0) == '+') {
                ec = ec.substring(1, ec.length());
                valorAux = ec;
            } else {
                valorAux = ec;
            }
        } else {
            valorAux = "0";
        }
        setzAuxT(Double.valueOf(valorAux));
    }

    public static void encontrarValores(String ec) {
        try {
            int aux = 0, auxF = 0;
            String A = "0", B = "0", C = "0", D = "0";
            String eX = "1", eY = "1", eZ = "1";
            boolean existe = false;
            boolean existeP = false;
            for (int i = 0; i < ec.length(); i++) {
                if (ec.charAt(i) == 'x') {
                    aux = i;
                    auxF = aux;
                    A = "";
                    existe = true;
                    for (int j = i - 1; j >= 0; j--) {
                        if (Character.isDigit(ec.charAt(j)) || ec.charAt(j) == '-' || ec.charAt(j) == '+') {
                            A = ec.charAt(j) + A;
                            auxF--;
                        } else {
                            j = 0;
                        }
                    }
                    if ((ec.charAt(i) == 'x') && ((i + 1) < (ec.length())) && (ec.charAt(i + 1) == '^')) {
                        eX = "";
                        existeP = true;
                        while (Character.isDigit(ec.charAt(i + 2))) {
                            eX = ec.charAt(i + 2) + eX;
                            i++;
                            aux = i;
                        }

                    }
                }
                if (existe && A.equals("")) {
                    A = "1";
                    existe = false;
                }
                if (A.charAt(0) == '+') {
                    A = A.substring(1, A.length());
                }
            }
            if (existeP) {
                ec = ec.substring(0, auxF) + ec.substring(aux + 2, ec.length());
                existeP = false;
            } else {
                ec = ec.substring(0, auxF) + ec.substring(aux + 1, ec.length());
            }
            if (A.length() == 1 && A.charAt(0) == '-') {
                A = "-1";
            }
            for (int i = 0; i < ec.length(); i++) {
                if (ec.charAt(i) == 'y') {
                    aux = i;
                    auxF = aux;
                    B = "";
                    existe = true;
                    for (int j = i - 1; j >= 0; j--) {
                        if (Character.isDigit(ec.charAt(j)) || ec.charAt(j) == '-' || ec.charAt(j) == '+') {
                            B = ec.charAt(j) + B;
                            auxF--;
                        } else {
                            j = 0;
                        }
                    }
                    if ((ec.charAt(i) == 'y') && ((i + 1) < (ec.length())) && (ec.charAt(i + 1) == '^')) {
                        eY = "";
                        existeP = true;
                        while (Character.isDigit(ec.charAt(i + 2))) {
                            eY = ec.charAt(i + 2) + eY;
                            i++;
                            aux = i;
                        }
                    }
                }
                if (existe && B.equals("")) {
                    B = "1";
                    existe = false;
                }
                if (B.charAt(0) == '+') {
                    B = B.substring(1, B.length());
                }
            }
            if (existeP) {
                ec = ec.substring(0, auxF) + ec.substring(aux + 2, ec.length());
                existeP = false;
            } else {
                ec = ec.substring(0, auxF) + ec.substring(aux + 1, ec.length());
            }
            if (B.length() == 1 && B.charAt(0) == '-') {
                B = "-1";
            }
            for (int i = 0; i < ec.length(); i++) {
                if (ec.charAt(i) == 'z') {
                    aux = i;
                    auxF = aux;
                    C = "";
                    existe = true;
                    for (int j = i - 1; j >= 0; j--) {
                        if (Character.isDigit(ec.charAt(j)) || ec.charAt(j) == '-' || ec.charAt(j) == '+') {
                            C = ec.charAt(j) + C;
                            auxF--;
                        } else {
                            j = 0;
                        }
                    }
                    if ((ec.charAt(i) == 'z') && ((i + 1) < (ec.length())) && (ec.charAt(i + 1) == '^')) {
                        eZ = "";
                        existeP = true;
                        while (Character.isDigit(ec.charAt(i + 2))) {
                            eZ = ec.charAt(i + 2) + eZ;
                            i++;
                            aux = i;
                        }

                    }
                }
                if (existe && C.equals("")) {
                    C = "1";
                    existe = false;
                }
                if (C.charAt(0) == '+') {
                    C = C.substring(1, C.length());
                }
            }
            if (existeP) {
                ec = ec.substring(0, auxF) + ec.substring(aux + 2, ec.length());
                existeP = false;
            } else {
                ec = ec.substring(0, auxF) + ec.substring(aux + 1, ec.length());
            }
            if (C.length() == 1 && C.charAt(0) == '-') {
                C = "-1";
            }
            if ((ec.length() > 0) && (ec.charAt(0) == '+')) {
                D = ec.substring(1, ec.length());
            } else if (!D.equals("0")) {
                D = ec.substring(0, ec.length());
            }
            setA(Integer.parseInt(A));
            setB(Integer.parseInt(B));
            setC(Integer.parseInt(C));
            setD(Integer.parseInt(D));
            setEx(Integer.parseInt(eX));
            setEy(Integer.parseInt(eY));
            setEz(Integer.parseInt(eZ));
        } catch (Exception e) {
        }

    }

    public void mouseDragged(MouseEvent e) {
        X = e.getX();
        Y = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (pressCtrl && (e.getButton() == MouseEvent.BUTTON1)) {
            mov = 0.2;
        }
    }

    public void mousePressed(MouseEvent e) {
        if (!pressCtrl && (e.getButton() == MouseEvent.BUTTON1)) {
            mov = 0.0;
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public static double getA() {
        return a;
    }

    public static void setA(double a) {
        Calculus.a = a;
    }

    public static double getB() {
        return b;
    }

    public static void setB(double b) {
        Calculus.b = b;
    }

    public static double getC() {
        return c;
    }

    public static void setC(double c) {
        Calculus.c = c;
    }

    public static double getD() {
        return d;
    }

    public static void setD(double d) {
        Calculus.d = d;
    }

    public static double getEx() {
        return ex;
    }

    public static void setEx(double aEx) {
        ex = aEx;
    }

    public static double getEy() {
        return ey;
    }

    public static void setEy(double aEy) {
        ey = aEy;
    }

    public static double getEz() {
        return ez;
    }

    public static void setEz(double aEz) {
        ez = aEz;
    }

    public static double getxFacT() {
        return xFacT;
    }

    public static void setxFacT(double axFacT) {
        xFacT = axFacT;
    }

    public static double getxAuxT() {
        return xAuxT;
    }

    public static void setxAuxT(double axAuxT) {
        xAuxT = axAuxT;
    }

    public static double getyFacT() {
        return yFacT;
    }

    public static void setyFacT(double ayFacT) {
        yFacT = ayFacT;
    }

    public static double getyAuxT() {
        return yAuxT;
    }

    public static void setyAuxT(double ayAuxT) {
        yAuxT = ayAuxT;
    }

    public static double getzFacT() {
        return zFacT;
    }

    public static void setzFacT(double azFacT) {
        zFacT = azFacT;
    }

    public static double getzAuxT() {
        return zAuxT;
    }

    public static void setzAuxT(double azAuxT) {
        zAuxT = azAuxT;
    }

    public static String getxTrigoT() {
        return xTrigoT;
    }

    public static void setxTrigoT(String axTrigoT) {
        xTrigoT = axTrigoT;
    }

    public static String getyTrigoT() {
        return yTrigoT;
    }

    public static void setyTrigoT(String ayTrigoT) {
        yTrigoT = ayTrigoT;
    }

    public static String getzTrigoT() {
        return zTrigoT;
    }

    public static void setzTrigoT(String azTrigoT) {
        zTrigoT = azTrigoT;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        //Arriba -1, Abajo 1
        if (!pressShift) {
            if ((e.getWheelRotation() == -1) && (S > 0.05f)) {
                S = (((int) ((S - 0.02) * 100)) / 100.0);
            } else if (e.getWheelRotation() == 1) {
                S = (((int) ((S + 0.02) * 100)) / 100.0);
            }
        }
        if (pressShift) {
            if ((e.getWheelRotation() == -1) && (mov > 0.05f)) {
                mov = (((int) ((mov - 0.1) * 100)) / 100.0);
            } else if (e.getWheelRotation() == 1) {
                mov = (((int) ((mov + 0.1) * 100)) / 100.0);
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            pressCtrl = true;
        }
        if (e.getKeyCode() == 16) {
            pressShift = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            pressCtrl = false;
        }
        if (e.getKeyCode() == 16) {
            pressShift = false;
        }
    }

    public static boolean isIntersectar() {
        return intersectar;
    }

    public static void setIntersectar(boolean aIntersectar) {
        intersectar = aIntersectar;
    }
}
