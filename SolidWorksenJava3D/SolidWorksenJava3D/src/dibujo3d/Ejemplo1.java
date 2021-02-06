/*
 * Ejemplo1.java
 *
 * Created on 29 de septiembre de 2008, 09:30 AM
 */
package dibujo3d;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import java.io.FileNotFoundException;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;

/**
 *
 * @author  rvirtual
 */
public class Ejemplo1 extends javax.swing.JApplet {

    private SimpleUniverse simpleU=null;
    private TransformGroup obj1,  obj2, obj3, obj4;

    /** Initializes the applet Ejemplo1 */
    public void init() {
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    initComponents();
                    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
                    Canvas3D canvas3D = new Canvas3D(config);

                    jPanel2.add("Center", canvas3D);

                    BranchGroup scene = createSceneGraph();
                    scene.compile();

                    simpleU = new SimpleUniverse(canvas3D);
                    simpleU.getViewingPlatform().setNominalViewingTransform();
                    simpleU.addBranchGraph(scene);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();

        //Inicializar objetos
        obj1 = new TransformGroup();
        obj1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        obj1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        obj2 = new TransformGroup();
        obj2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        obj2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        obj3 = new TransformGroup();
        obj3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        obj3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        obj4 = new TransformGroup();
        obj4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        obj4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        obj2.addChild(loadGeometryWRL("ensamblaje4.wrl"));
        obj3.addChild(loadGeometryWRL("ensamblaje2.wrl"));
        obj1.addChild(loadGeometryWRL("ensamblaje3.wrl"));
        obj4.addChild(loadGeometryWRL("ensamblaje5.wrl"));
        objRoot.addChild(obj1);
        //obj2.addChild(loadGeometryWRL("ensamblaje1.wrl"));
        obj1.addChild(obj2);
        obj1.addChild(obj3);
        obj1.addChild(obj4);
        
       Transform3D actual = new Transform3D();
       obj1.getTransform(actual);
    Transform3D inc = new Transform3D();
    
    inc.setTranslation(new Vector3f (0.0f, -0.20f,0.0f ));
    actual.mul(inc);
    obj1.setTransform(actual);
    obj2.getTransform(actual);
    //Transform3D inc = new Transform3D();
    inc.rotY(Math.PI);
    inc.setTranslation(new Vector3f (0.08f, 0.01f,-0.06f ));
    actual.mul(inc);
    obj2.setTransform(actual);
    
       obj3.getTransform(actual);
       Transform3D inc1 = new Transform3D();
    
    inc.rotZ(Math.PI /2 );
    inc1.rotX(-Math.PI / 2);
    inc.setTranslation(new Vector3f (0.08f, 0.09f,-0.135f ));
    actual.mul(inc);
    actual.mul(inc1);
    obj3.setTransform(actual);

      obj4.getTransform(actual);
      inc.rotX(Math.PI/2);
      inc.setTranslation(new Vector3f (0.1025f, 0.135f,-0.10f));
      actual.mul(inc);
obj4.setTransform(actual);

        //luces
        objRoot.addChild(luces());

        //fondo
        objRoot.addChild(fondo());

        //piso
        objRoot.addChild(piso());
        
        return objRoot;

    }

    public BranchGroup loadGeometryWRL(String geometryURL) {
        BranchGroup objLoad = new BranchGroup();

        VrmlLoader wrl = new VrmlLoader();
        try {
            objLoad = wrl.load(geometryURL).getSceneGroup();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (ParsingErrorException ex) {
            ex.printStackTrace();
        } catch (IncorrectFormatException ex) {
            ex.printStackTrace();
        }
        return objLoad;
    }

    private TransformGroup fondo() {
        TransformGroup objRoot = new TransformGroup();
        Background font = new Background(new Color3f(0.5f, 0.6f, 0.9f));
        font.setApplicationBounds(new BoundingSphere(new Point3d(), 100.0));
        objRoot.addChild(font);
        return objRoot;
    }

    private TransformGroup luces() {
        TransformGroup objRoot = new TransformGroup();

        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 5), 100.0);
        Color3f lightColor = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light1Direction = new Vector3f(0.0f, -1.0f, -1f);

        DirectionalLight luz1 = new DirectionalLight(lightColor, light1Direction);
        luz1.setInfluencingBounds(bounds);
        objRoot.addChild(luz1);

        AmbientLight luz2 = new AmbientLight(lightColor);
        luz2.setInfluencingBounds(bounds);
        objRoot.addChild(luz2);

        return objRoot;
    }

    private TransformGroup piso() {
        TransformGroup sueloTransf = new TransformGroup();

        int tamano = 50;
        Point3f[] vertices = new Point3f[tamano * tamano];

        float inicio = -20.0f;
        float x = inicio;
        float z = inicio;

        float salto = 1.0f;

        int[] indices = new int[(tamano - 1) * (tamano - 1) * 4];
        int n = 0;

        Color3f blanco = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f negro = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f[] colors = {blanco, negro};

        int[] colorindices = new int[indices.length];

        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                vertices[i * tamano + j] = new Point3f(x, -1.0f, z);
                z += salto;
                if (i < (tamano - 1) && j < (tamano - 1)) {
                    int cindex = (i % 2 + j) % 2;
                    colorindices[n] = cindex;
                    indices[n++] = i * tamano + j;
                    colorindices[n] = cindex;
                    indices[n++] = i * tamano +
                            (j + 1);
                    colorindices[n] = cindex;
                    indices[n++] = (i + 1) *
                            tamano + (j + 1);
                    colorindices[n] = cindex;
                    indices[n++] = (i + 1) *
                            tamano + j;
                }
            }
            z = inicio;
            x += salto;
        }

        IndexedQuadArray geom = new IndexedQuadArray(vertices.length,
                GeometryArray.COORDINATES |
                GeometryArray.COLOR_3,
                indices.length);
        geom.setCoordinates(0, vertices);
        geom.setCoordinateIndices(0, indices);
        geom.setColors(0, colors);
        geom.setColorIndices(0, colorindices);

        Shape3D suelo = new Shape3D(geom);
        sueloTransf.addChild(suelo);

        return sueloTransf;
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Rot. Pieza"));

        jButton1.setText("+x");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("-x");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("+y");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("-y");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("+z");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("-z");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Camara"));

        jButton7.setText("+");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("-");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton19.setText("↑");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setText("↓");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton19)
                    .addComponent(jButton20)))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Mov. Dibujo"));

        jButton9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton9.setText("→");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton10.setText("↓");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton11.setText("←");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton12.setText("↑");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton13.setText("Z+");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton15.setText("X+Y+");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton16.setText("X-Y+");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton17.setText("X+Y-");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton18.setText("X-Y-");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton14.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton14.setText("Z-");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton11, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton12)
                            .addComponent(jButton10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(jButton14)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    Transform3D actual = new Transform3D();
    obj1.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.rotX(Math.PI/16);
    actual.mul(inc);
    obj1.setTransform(actual);
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    Transform3D actual = new Transform3D();
    obj1.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.rotX(-Math.PI/16);
    actual.mul(inc);
    obj1.setTransform(actual);
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    Transform3D actual = new Transform3D();
    obj1.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.rotY(Math.PI / 16);
    actual.mul(inc);
    obj1.setTransform(actual);
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    Transform3D actual = new Transform3D();
    obj1.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.rotY(-Math.PI / 16);
    actual.mul(inc);
    obj1.setTransform(actual);
}//GEN-LAST:event_jButton4ActionPerformed

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    Transform3D actual = new Transform3D();
    obj1.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.rotZ(Math.PI / 16);
    actual.mul(inc);
    obj1.setTransform(actual);
}//GEN-LAST:event_jButton5ActionPerformed

private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
    Transform3D actual = new Transform3D();
    obj1.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.rotZ(-Math.PI / 16);
    actual.mul(inc);
    obj1.setTransform(actual);
}//GEN-LAST:event_jButton6ActionPerformed

private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
    
    //Obtener TransformGroup camara
    TransformGroup universo =
            simpleU.getViewingPlatform().getViewPlatformTransform();
    
    //Obtener posicion de la camara con un transform3d
    Transform3D actual = new Transform3D();
    universo.getTransform(actual);
    
    //Crear un incremento
    Transform3D inc = new Transform3D();
    inc.set(new Vector3d(0,0,0.1));
    //Multiplicar posicion actual por incremento
    actual.mul(inc);
    //Escribir resultado de la nueva posicion
    universo.setTransform(actual);
}//GEN-LAST:event_jButton7ActionPerformed

private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    //Obtener TransformGroup camara
    TransformGroup universo =
            simpleU.getViewingPlatform().getViewPlatformTransform();
    
    //Obtener posicion de la camara con un transform3d
    Transform3D actual = new Transform3D();
    universo.getTransform(actual);
    
    //Crear un incremento
    Transform3D inc = new Transform3D();
    inc.set(new Vector3d(0,0,-0.1));
    //Multiplicar posicion actual por incremento
    actual.mul(inc);
    //Escribir resultado de la nueva posicion
    universo.setTransform(actual);
}//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
      Transform3D actual = new Transform3D();
    obj3.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,-0.001f));
    actual.mul(inc);
    obj3.setTransform(actual);   
    obj4.getTransform(actual);
    
    inc.setTranslation(new Vector3f(0.001f,0.0f,0.0f));
    actual.mul(inc);
    obj4.setTransform(actual);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
     Transform3D actual = new Transform3D();
    obj4.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,-0.005f));
    actual.mul(inc);
    obj4.setTransform(actual);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
     Transform3D actual = new Transform3D();
    obj3.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,0.001f));
    actual.mul(inc);
    obj3.setTransform(actual);
    obj4.getTransform(actual);
    
    inc.setTranslation(new Vector3f(-0.001f,0.0f,0.0f));
    actual.mul(inc);
    obj4.setTransform(actual);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
     Transform3D actual = new Transform3D();
    obj2.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,-0.001f));
    actual.mul(inc);
    obj2.setTransform(actual);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
       Transform3D actual = new Transform3D();
    obj2.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,0.001f));
    actual.mul(inc);
    obj2.setTransform(actual);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
     Transform3D actual = new Transform3D();
    obj4.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,0.005f));
    actual.mul(inc);
    obj4.setTransform(actual);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
     Transform3D actual = new Transform3D();
    obj2.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,-0.001f));
    actual.mul(inc);
    obj2.setTransform(actual);
     
   obj3.getTransform(actual);
    
    inc.setTranslation(new Vector3f(0.0f,0.0f,0.001f));
    actual.mul(inc);
    obj3.setTransform(actual);
    obj4.getTransform(actual);
    
    inc.setTranslation(new Vector3f(-0.001f,0.0f,0.0f));
    actual.mul(inc);
    obj4.setTransform(actual);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
     Transform3D actual = new Transform3D();
    obj2.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,0.001f));
    actual.mul(inc);
    obj2.setTransform(actual);
     
     obj3.getTransform(actual);
    
    inc.setTranslation(new Vector3f(0.0f,0.0f,-0.001f));
    actual.mul(inc);
    obj3.setTransform(actual);
    obj4.getTransform(actual);
    
    inc.setTranslation(new Vector3f(0.001f,0.0f,0.0f));
    actual.mul(inc);
    obj4.setTransform(actual);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
     Transform3D actual = new Transform3D();
    obj2.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,0.001f));
    actual.mul(inc);
    obj2.setTransform(actual);
     
    obj3.getTransform(actual);
    
    inc.setTranslation(new Vector3f(0.0f,0.0f,0.001f));
    actual.mul(inc);
    obj3.setTransform(actual);
    obj4.getTransform(actual);
    
    inc.setTranslation(new Vector3f(-0.001f,0.0f,0.0f));
    actual.mul(inc);
    obj4.setTransform(actual);    
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
      Transform3D actual = new Transform3D();
    obj2.getTransform(actual);
    Transform3D inc = new Transform3D();
    inc.setTranslation(new Vector3f(0.0f,0.0f,0.001f));
    actual.mul(inc);
    obj2.setTransform(actual);
     
     obj3.getTransform(actual);
    
    inc.setTranslation(new Vector3f(0.0f,0.0f,-0.001f));
    actual.mul(inc);
    obj3.setTransform(actual);
    obj4.getTransform(actual);
    
    inc.setTranslation(new Vector3f(0.001f,0.0f,0.0f));
    actual.mul(inc);
    obj4.setTransform(actual);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        //Obtener TransformGroup camara
    TransformGroup universo =
            simpleU.getViewingPlatform().getViewPlatformTransform();
    
    //Obtener posicion de la camara con un transform3d
    Transform3D actual = new Transform3D();
    universo.getTransform(actual);
    
    //Crear un incremento
    Transform3D inc = new Transform3D();
    inc.set(new Vector3d(0,0.05,0));
    //Multiplicar posicion actual por incremento
    actual.mul(inc);
    //Escribir resultado de la nueva posicion
    universo.setTransform(actual);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
         //Obtener TransformGroup camara
    TransformGroup universo =
            simpleU.getViewingPlatform().getViewPlatformTransform();
    
    //Obtener posicion de la camara con un transform3d
    Transform3D actual = new Transform3D();
    universo.getTransform(actual);
    
    //Crear un incremento
    Transform3D inc = new Transform3D();
    inc.set(new Vector3d(0,-0.05,0));
    //Multiplicar posicion actual por incremento
    actual.mul(inc);
    //Escribir resultado de la nueva posicion
    universo.setTransform(actual);
    }//GEN-LAST:event_jButton20ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables
}
