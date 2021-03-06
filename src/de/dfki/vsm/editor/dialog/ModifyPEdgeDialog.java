package de.dfki.vsm.editor.dialog;

import de.dfki.vsm.editor.Editor;
import de.dfki.vsm.editor.util.AltStartNodeManager;
import de.dfki.vsm.model.sceneflow.Node;
import de.dfki.vsm.model.sceneflow.PEdge;
import de.dfki.vsm.model.sceneflow.SuperNode;
import de.dfki.vsm.util.ios.ResourceLoader;
import de.dfki.vsm.util.tpl.TPLTuple;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * @author Patrick Gebhard
 * @author Gregor Mehlmann
 */
public class ModifyPEdgeDialog extends Dialog {

    // Data model components
    private final PEdge mPEdge;
    private final Node mSourceNode;
    private final Node mTargetNode;
    //
    private HashMap<PEdge, JTextField> mPEdgeMap
            = new HashMap<PEdge, JTextField>();
    //
    private final AltStartNodeManager mAltStartNodeManager;
    // GUI-Components
    private JPanel mHeaderPanel;
    private JLabel mHeaderLabel;
    //
    private JPanel mButtonPanel;
    private JButton mOkButton;
    private JButton mCancelButton;
    private JButton mNormButton;
    private JButton mUniButton;
    //
    private JPanel mAltStartNodePanel;
    private JLabel mAltStartNodeLabel;
    private JList mAltStartNodeList;
    private JScrollPane mAltStartNodeScrollPane;
    private JButton mAddAltStartNodeButton;
    private JButton mRemoveAltStartNodeButton;
    private JButton mEditAltStartNodeButton;
    //
    private JPanel mEdgeProbPanel;
    //
    private JPanel mRestPanel;
    private JLabel mRestLabel;
    private JTextField mRestField;

    public ModifyPEdgeDialog(PEdge edge) {
        super(Editor.getInstance(), "Modify Probability Edge", true);
        mPEdge = edge;
        mSourceNode = mPEdge.getSourceNode();
        mTargetNode = mPEdge.getTargetNode();
        // Fill the map with the data
        for (PEdge pedge : mSourceNode.getPEdgeList()) {
            mPEdgeMap.put(pedge, /*new JTextField("", 3)*/ null);
        }

        // TODO: move to EdgeDialog
        mAltStartNodeManager = new AltStartNodeManager(mPEdge);
        // Init the GUI-Components
        initComponents();
        //
        loadAltStartNodeMap();
    }

//    private void initComponents() {
//        // Init button panel
//        initButtonPanel();
//        // Init probability panel
//        initEdgeProbabilityPanel();
//        // Init main panel
//        mMainPanel.setLayout(new BoxLayout(mMainPanel, BoxLayout.Y_AXIS));
//        mMainPanel.add(mEdgeProbPanel);
//        mMainPanel.add(mButtonPanel);
//        // Pack components and register key listeners
//        packComponents();
//    }
//
//    private void initButtonPanel() {
//        mOkButton = new JButton("Ok");
//        mOkButton.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent evt) {
//                okActionPerformed();
//            }
//        });
//        mCancelButton = new JButton("Cancel");
//        mCancelButton.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent evt) {
//                cancelActionPerformed();
//            }
//        });
//        mNormButton = new JButton("Normalize");
//        mNormButton.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent evt) {
//                normActionPerformed();
//            }
//        });
//        mUniButton = new JButton("Uniform");
//        mUniButton.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent evt) {
//                uniActionPerformed();
//            }
//        });
//        mButtonPanel = new JPanel();
//        mButtonPanel.setLayout(new BoxLayout(mButtonPanel, BoxLayout.X_AXIS));
//        mButtonPanel.add(Box.createHorizontalGlue());
//        mButtonPanel.add(mNormButton);
//        mButtonPanel.add(mUniButton);
//        mButtonPanel.add(mCancelButton);
//        mButtonPanel.add(mOkButton);
//        mButtonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
//    }
//
//    private void initEdgeProbabilityPanel() {
//        mEdgeProbPanel = new JPanel();
//        mEdgeProbPanel.setLayout(new BoxLayout(mEdgeProbPanel, BoxLayout.Y_AXIS));
//        //
//        for (PEdge pedge : mPEdgeMap.keySet()) {
//            // Init the description label
//            JLabel pedgeDescription = new JLabel(
//                    mSourceNode.getName() + " ( " +
//                    mSourceNode.getId() + " ) " + " \u2192  " +
//                    pedge.getTargetNode().getName() + " ( " +
//                    pedge.getTargetNode().getId() + " ) ");
//
//            JTextField probField = new JTextField(Integer.toString(pedge.getProbability()), 3);
//            probField.setMinimumSize(new Dimension(40, 20));
//            probField.setPreferredSize(new Dimension(40, 20));
//            probField.setMaximumSize(new Dimension(40, 20));
//            //
//            mPEdgeMap.put(pedge, probField);
//            //
//            JPanel pedgePanel = new JPanel();
//            pedgePanel.setLayout(new BoxLayout(pedgePanel, BoxLayout.X_AXIS));
//            pedgePanel.add(Box.createRigidArea(new Dimension(5, 0)));
//            pedgePanel.add(pedgeDescription);
//            pedgePanel.add(Box.createRigidArea(new Dimension(10, 0)));
//            pedgePanel.add(Box.createHorizontalGlue());
//            pedgePanel.add(probField);
//            pedgePanel.add(Box.createRigidArea(new Dimension(5, 0)));
//            mEdgeProbPanel.add(pedgePanel);
//        }
//    }
    private void initComponents() {

        // Init probability panel
        initEdgeProbabilityPanel();
        // Init button panel
        initButtonPanel(); 
        // Init alternative start node panel
        initAltStartNodePanel();
        // Init main panel
        // Init main panel
        mMainPanel.setLayout(new BoxLayout(mMainPanel, BoxLayout.Y_AXIS));
        //
        addCompoment(mEdgeProbPanel, 240, 70 + 25 * mPEdgeMap.size());
        addCompoment(mAltStartNodePanel, 240, 100);
        addCompoment(mButtonPanel, 240, 40);
        packComponents(240, 140 + 70 + 25 * mPEdgeMap.size());
    }

    private void initButtonPanel() {
        
        // Normalize button
        mNormButton = new JButton("Normalize");
        mNormButton.setMinimumSize(new Dimension(120, 20));
        mNormButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                normalizeActionPerformed();
            }
        });
        // Uniform button
        mUniButton = new JButton("Uniform");
        mUniButton.setMinimumSize(new Dimension(120, 20));
        mUniButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                uniformActionPerformed();
            }
        });
        // Ok button
        mOkButton = new JButton("Ok");
        mOkButton.setMinimumSize(new Dimension(120, 20));
        mOkButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okActionPerformed();
            }
        });
        // Cancel button
        mCancelButton = new JButton("Cancel");
        mCancelButton.setMinimumSize(new Dimension(120, 20));
        mCancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelActionPerformed();
            }
        });
        
        // Button panel
        JPanel firstButtonPanel = new JPanel(null);        
        firstButtonPanel.setLayout(new BoxLayout(firstButtonPanel, BoxLayout.LINE_AXIS));
        firstButtonPanel.add(mNormButton);
        firstButtonPanel.add(mUniButton);
        
         // Button panel
        JPanel secondButtonPanel = new JPanel(null);        
        secondButtonPanel.setLayout(new BoxLayout(secondButtonPanel, BoxLayout.LINE_AXIS));
        secondButtonPanel.add(mOkButton);
        secondButtonPanel.add(mCancelButton);
        
        mButtonPanel = new JPanel(null);
        mButtonPanel.setLayout(new BoxLayout(mButtonPanel, BoxLayout.Y_AXIS));
        mButtonPanel.add(firstButtonPanel);
        mButtonPanel.add(secondButtonPanel);
        
        
        /*

        
        // Normalize button
        mNormButton = new JButton("Normalize");
        mNormButton.setBounds(10, 10, 80, 20);
        mNormButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                normalizeActionPerformed();
            }
        });
        // Uniform button
        mUniButton = new JButton("Uniform");
        mUniButton.setBounds(90, 10, 280, 50);
        mUniButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                uniformActionPerformed();
            }
        });
        // Ok button
        mOkButton = new JButton("Ok");
        mOkButton.setBounds(170, 10, 70, 20);
        mOkButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okActionPerformed();
            }
        });
        // Cancel button
        mCancelButton = new JButton("Cancel");
        mCancelButton.setBounds(240, 10, 70, 20);
        mCancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelActionPerformed();
            }
        });
        // Button panel
        mButtonPanel = new JPanel(null);
        //mButtonPanel.add(mNormButton);
        //mButtonPanel.add(mUniButton);
        mButtonPanel.add(mOkButton);
        mButtonPanel.add(mCancelButton);
                
       */
    }

    private void initEdgeProbabilityPanel() {
        // Init header label
        mHeaderLabel = new JLabel("Edge Probabilities:");
        mHeaderLabel.setMinimumSize(new Dimension(200, 25));
        mHeaderLabel.setPreferredSize(new Dimension(200, 25));
        mHeaderLabel.setMaximumSize(new Dimension(200, 25));
        // Init header panel
        mHeaderPanel = new JPanel();
        mHeaderPanel.setMinimumSize(new Dimension(280, 25));
        mHeaderPanel.setPreferredSize(new Dimension(280, 25));
        mHeaderPanel.setMaximumSize(new Dimension(280, 25));
        mHeaderPanel.setLayout(new BoxLayout(mHeaderPanel, BoxLayout.X_AXIS));
        mHeaderPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        mHeaderPanel.add(mHeaderLabel);
        // Init edge probability panel
        mEdgeProbPanel = new JPanel();
        mEdgeProbPanel.setLayout(new BoxLayout(mEdgeProbPanel, BoxLayout.Y_AXIS));
        mEdgeProbPanel.add(Box.createRigidArea(new Dimension(280, 5)));
        mEdgeProbPanel.add(mHeaderPanel);
        mEdgeProbPanel.add(Box.createRigidArea(new Dimension(280, 5)));

        //
        for (PEdge pedge : mPEdgeMap.keySet()) {
            // Init description label
            JLabel pedgeDescription = new JLabel(
                    mSourceNode.getName() + " ( "
                    + mSourceNode.getId() + " ) " + " \u2192  "
                    + pedge.getTargetNode().getName() + " ( "
                    + pedge.getTargetNode().getId() + " ) ");
            pedgeDescription.setMinimumSize(new Dimension(160, 25));
            pedgeDescription.setPreferredSize(new Dimension(160, 25));
            pedgeDescription.setMaximumSize(new Dimension(160, 25));
            // Compute initial probability
            int prob = pedge.getProbability();
            if (prob == Integer.MIN_VALUE) {
                prob = 100;
            }
            // Init probability text field
            JTextField probField = new JTextField(new IntegerDocument(), String.valueOf(prob), 3);
            probField.setMinimumSize(new Dimension(40, 25));
            probField.setPreferredSize(new Dimension(40, 25));
            probField.setMaximumSize(new Dimension(40, 25));
            probField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent e) {
                    int sum = 0;
                    for (JTextField textField : mPEdgeMap.values()) {
                        try {
                            sum += Integer.valueOf(textField.getText().trim()).intValue();
                        } catch (NumberFormatException es) {
                            //
                        }
                    }
                    // Set the rest to the rest text field
                    mRestField.setText(Integer.valueOf(100 - sum).toString());
                }
            });
            // Add the text field to the mapping
            mPEdgeMap.put(pedge, probField);
            // Init probability panel
            JPanel pedgePanel = new JPanel();
            pedgePanel.setMinimumSize(new Dimension(280, 25));
            pedgePanel.setPreferredSize(new Dimension(280, 25));
            pedgePanel.setMaximumSize(new Dimension(280, 25));
            pedgePanel.setLayout(new BoxLayout(pedgePanel, BoxLayout.X_AXIS));
            pedgePanel.add(Box.createRigidArea(new Dimension(20, 0)));
            pedgePanel.add(pedgeDescription);
            pedgePanel.add(Box.createRigidArea(new Dimension(15, 0)));
            //pedgePanel.add(Box.createHorizontalGlue());
            pedgePanel.add(probField);
            pedgePanel.add(Box.createRigidArea(new Dimension(10, 0)));
            mEdgeProbPanel.add(pedgePanel);
        }
        // Init rest panel
        mRestPanel = new JPanel();
        mRestPanel.setLayout(new BoxLayout(mRestPanel, BoxLayout.X_AXIS));
        mRestPanel.setMinimumSize(new Dimension(280, 25));
        mRestPanel.setPreferredSize(new Dimension(280, 25));
        mRestPanel.setMaximumSize(new Dimension(280, 25));
        //
        mRestLabel = new JLabel("Rest:");
        mRestLabel.setMinimumSize(new Dimension(160, 25));
        mRestLabel.setPreferredSize(new Dimension(160, 25));
        mRestLabel.setMaximumSize(new Dimension(160, 25));
        //
        mRestField = new JTextField(3);
        mRestField.setDocument(new IntegerDocument());
        mRestField.setEditable(false);
        mRestField.setEnabled(false);
        mRestField.setMinimumSize(new Dimension(40, 25));
        mRestField.setPreferredSize(new Dimension(40, 25));
        mRestField.setMaximumSize(new Dimension(40, 25));

        mRestPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        mRestPanel.add(mRestLabel);
        mRestPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        //mRestPanel.add(Box.createHorizontalGlue());
        mRestPanel.add(mRestField);
        mRestPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        // Add the rest panel
        mEdgeProbPanel.add(Box.createRigidArea(new Dimension(280, 5)));
        mEdgeProbPanel.add(mRestPanel);
        mEdgeProbPanel.add(Box.createRigidArea(new Dimension(280, 5)));
        // Highlight the prbability text field of the current edge
        mPEdgeMap.get(mPEdge).setCaretPosition(0);
        mPEdgeMap.get(mPEdge).setForeground(Color.red);
        mPEdgeMap.get(mPEdge).setForeground(Color.red);
    }
    
    protected void initAltStartNodePanel() {
        JPanel titleContainer; 
        JPanel buttonsContainer; 
        JPanel startNodeContainer; 
        
        // Init alternative start node label
        mAltStartNodeLabel = new JLabel("Alternative Start Nodes:");        
        
        // Init alternative start node list
        mAltStartNodeList = new JList(new DefaultListModel());
        mAltStartNodeScrollPane = new JScrollPane(mAltStartNodeList);
        
        // Init alternative start node buttons300
        mAddAltStartNodeButton = new JButton(ResourceLoader.loadImageIcon("/res/img/new/plus.png"));  
        mAddAltStartNodeButton.setMaximumSize(new Dimension(20, 20));
        mAddAltStartNodeButton.setPreferredSize(new Dimension(20, 20));
	mAddAltStartNodeButton.setMinimumSize(new Dimension(20, 20)); 
        mAddAltStartNodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAltStartNode();
            }
        });
        
        mRemoveAltStartNodeButton = new JButton(ResourceLoader.loadImageIcon("/res/img/new/minus.png"));
        mRemoveAltStartNodeButton.setMaximumSize(new Dimension(20, 20));
        mRemoveAltStartNodeButton.setPreferredSize(new Dimension(20, 20));
	mRemoveAltStartNodeButton.setMinimumSize(new Dimension(20, 20));  
        mRemoveAltStartNodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeAltStartNode();
            }
        });
        
        mEditAltStartNodeButton = new JButton(ResourceLoader.loadImageIcon("/res/img/new/edit.png"));
        mEditAltStartNodeButton.setMaximumSize(new Dimension(20, 20));
        mEditAltStartNodeButton.setPreferredSize(new Dimension(20, 20));
	mEditAltStartNodeButton.setMinimumSize(new Dimension(20, 20)); 
        mEditAltStartNodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editAltStartNode();
            }
        });      
        
        titleContainer = new JPanel(null);
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.X_AXIS));
        titleContainer.setAlignmentX(LEFT_ALIGNMENT);
        titleContainer.add(mAltStartNodeLabel);
        titleContainer.add(Box.createRigidArea(new Dimension(1000, 20)));        
        
        buttonsContainer = new JPanel(null);
        buttonsContainer.setLayout(new BoxLayout(buttonsContainer, BoxLayout.Y_AXIS));   
        buttonsContainer.setMaximumSize(new Dimension(20, 60));
        buttonsContainer.add(mAddAltStartNodeButton);
        buttonsContainer.add(mRemoveAltStartNodeButton);
        buttonsContainer.add(mEditAltStartNodeButton);
        
        startNodeContainer = new JPanel(null);
        startNodeContainer.setLayout(new BoxLayout(startNodeContainer, BoxLayout.X_AXIS));
        startNodeContainer.add(Box.createRigidArea(new Dimension(3, 20)));
        startNodeContainer.add(mAltStartNodeScrollPane);        
        startNodeContainer.add(buttonsContainer);        
        startNodeContainer.add(Box.createRigidArea(new Dimension(3, 20)));
        
        // Init alternative start node panel
        mAltStartNodePanel = new JPanel(null);        
        mAltStartNodePanel.setLayout(new BoxLayout(mAltStartNodePanel, BoxLayout.PAGE_AXIS));
        mAltStartNodePanel.setAlignmentX(CENTER_ALIGNMENT);
        mAltStartNodePanel.add(titleContainer);   
        //cmAltStartNodePanel.add(Box.createRigidArea(new Dimension(5, 5)));
        mAltStartNodePanel.add(startNodeContainer);
    }

    
    private void saveProbabilities() {
        if (!areProbabilitiesValid()) {
            return;
            
        }
        //Save the probabilities 
        Iterator it = mPEdgeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            PEdge edge = (PEdge) entry.getKey();
            JTextField field = (JTextField) entry.getValue();
            edge.setProbability(Integer.valueOf(field.getText().trim()));
        }
    }

    private boolean areProbabilitiesValid() {
        int sum = 0;
        for (JTextField textField : mPEdgeMap.values()) {
            try {
                sum += Integer.valueOf(textField.getText().trim()).intValue();
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return (sum == 100);
    }

    public void run() {
        setVisible(true);
        if (mPressedButton == Button.OK) {
            return;
        } else {
            return;
        }
    }

    public void normalizeActionPerformed() {
        // Compute the total sum of all probabilities
        int sum = 0;
        for (JTextField textField : mPEdgeMap.values()) {
            try {
                int value = Integer.valueOf(textField.getText().trim());
                if (value <= 0) {
                    //ERROR
                } else {
                    sum += value;
                }
            } catch (NumberFormatException e) {
                //ERROR
            }
        }

        for (JTextField textField : mPEdgeMap.values()) {
            double prob = Integer.valueOf(textField.getText().trim()).doubleValue();
            double ratiuon = (prob / Integer.valueOf(sum).doubleValue()) * 100.0d;
            textField.setText(Integer.valueOf((int) Math.round(ratiuon)).toString());
        }

    }

    public void uniformActionPerformed() {
        int numEdges = mPEdgeMap.size();
        int uniProb = 100 / numEdges;
        int restVal = 100 % numEdges;

        for (JTextField textField : mPEdgeMap.values()) {
            textField.setText(Integer.toString(uniProb));
        }
    }

    @Override
    public void okActionPerformed() {
        if (areProbabilitiesValid()) {
            saveProbabilities();
            saveAltStartNodeMap();
            dispose(Button.OK);
        }
    }

    @Override
    protected void cancelActionPerformed() {
        dispose(Button.CANCEL);
    }

    private void loadAltStartNodeMap() {
        mAltStartNodeManager.loadAltStartNodeMap();

        if (mPEdge.getTargetNode() instanceof SuperNode) {
            Iterator it = mAltStartNodeManager.mAltStartNodeMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                TPLTuple<String, Node> startNodePair = (TPLTuple<String, Node>) pairs.getKey();
                TPLTuple<String, Node> altStartNodePair = (TPLTuple<String, Node>) pairs.getValue();
                ((DefaultListModel) mAltStartNodeList.getModel()).addElement(
                        startNodePair.getFirst() + "/" + altStartNodePair.getFirst());
                ////System.err.println("loading start node "+startNodePair.getSecond());
                ////System.err.println("loading alt start node "+altStartNodePair.getSecond());
            }
        } else {
            mAddAltStartNodeButton.setEnabled(false);
            mRemoveAltStartNodeButton.setEnabled(false);
            mEditAltStartNodeButton.setEnabled(false);
            mAltStartNodeList.setEnabled(false);
            mAltStartNodeScrollPane.setEnabled(false);
        }
    }

    private void saveAltStartNodeMap() {
        mAltStartNodeManager.saveAltStartNodeMap();
    }

    private void addAltStartNode() {
        CreateAltStartNodeDialog dialog = new CreateAltStartNodeDialog(mAltStartNodeManager);
        dialog.run();
        ///
        ((DefaultListModel) mAltStartNodeList.getModel()).clear();
        Iterator it = mAltStartNodeManager.mAltStartNodeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            TPLTuple<String, Node> startNodePair = (TPLTuple<String, Node>) pairs.getKey();
            TPLTuple<String, Node> altStartNodePair = (TPLTuple<String, Node>) pairs.getValue();
            ((DefaultListModel) mAltStartNodeList.getModel()).addElement(
                    startNodePair.getFirst() + "/" + altStartNodePair.getFirst());
        }
    }

    private void removeAltStartNode() {
        String selectedValue = (String) mAltStartNodeList.getSelectedValue();
        if (selectedValue != null) {
            String[] idPair = selectedValue.split("/");
            String startNodeId = idPair[0];
            //String altStartNodeId = idPair[1];
            System.err.println("remove alt start node" + startNodeId);
            mAltStartNodeManager.removeAltStartNode(startNodeId);
            ((DefaultListModel) mAltStartNodeList.getModel()).removeElement(selectedValue);
        }
    }

    private void editAltStartNode() {
    }
    
    
    public JPanel getEdgeProbPanel(){
        return mEdgeProbPanel;
    }
    
    public JPanel getButtonPanel(){
        return mButtonPanel;
    }
    
    public JButton getNormButton(){
        return mNormButton;
    }
    
    
    public JButton getUniButton(){
        return mUniButton;
    }

    public JPanel getAltStartNodePanel(){
        return mAltStartNodePanel;
    }
    
    
    public HashMap<PEdge, JTextField> getPEdgeMap(){
        return mPEdgeMap;
    }
}
