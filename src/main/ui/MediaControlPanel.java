package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Timeline;
import model.TimelineController;

// Panel containing UI elements related to playback
public class MediaControlPanel extends JPanel implements ActionListener, ChangeListener, PropertyChangeListener {

    private static final int POSITION_LINE_UPDATE_DELAY = 10;

    private TimelineController timelineController;
    private JButton play;
    private ImageIcon playImage = null;
    private ImageIcon pauseImage = null;
    private Timer tickUpdateTimer;
    private JSlider scaleSlider;
    private JPanel leftAlignPanel;
    private JPanel rightAlignPanel;

    // EFFECTS: creates a MediaControlPanel with timers and initializes image icons and components
    public MediaControlPanel(TimelineController timelineController) {
        this.timelineController = timelineController;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        timelineController.addObserver(this);
        initFields();

        this.add(leftAlignPanel);
        this.add(rightAlignPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes media panel fields
    private void initFields() {
        leftAlignPanel = new JPanel();
        rightAlignPanel = new JPanel();

        leftAlignPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        rightAlignPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        tickUpdateTimer = new Timer(POSITION_LINE_UPDATE_DELAY, this);

        scaleSlider = new JSlider();
        scaleSlider.addChangeListener(this);
        scaleSlider.setPreferredSize(new Dimension(100, scaleSlider.getPreferredSize().height));
        copyTimelineScaleValue();

        play = new JButton();
        play.addActionListener(this);

        try {
            playImage = getImageIcon("lib/images/play.png");
            pauseImage = getImageIcon("lib/images/pause.png");
        } catch (Exception e) {
            System.out.println("Unable to load media icons: " + e.getMessage());
        }

        setPlayIcon(playImage);
        leftAlignPanel.add(scaleSlider);
        rightAlignPanel.add(play);
    }

    // EFFECTS: toggles playback
    public void togglePlay() {
        if (timelineController.isRunning()) {
            timelineController.pauseTimeline();
            tickUpdateTimer.stop();
            setPlayIcon(playImage);
            return;
        }

        timelineController.playTimeline();
        tickUpdateTimer.start();
        setPlayIcon(pauseImage);
    }

    // MODIFIES: this
    // EFFECTS: sets the render sliders value to that of the timeline render scale
    private void copyTimelineScaleValue() {
        int value = (int) (100 * timelineController.getTimeline().getHorizontalScale() / Timeline.MAX_HORIZONTAL_SCALE);
        scaleSlider.setValue(value);
    }

    // EFFECTS: returns the ImageIcon representation of the image at path
    private ImageIcon getImageIcon(String path) {
        try {
            return new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            System.out.println("Couldnt load image at " + path);
            return null;
        }
    }

    // MODIFES: this
    // EFFECTS: replaces play button icon with the specified icon
    private void setPlayIcon(ImageIcon icon) {
        play.setIcon(icon);
    }

    // MODIFIES: timeline singleton
    // EFFECTS: updates render scale according to slider
    private void updateScale() {
        double factor = (double) scaleSlider.getValue() / 100;

        double scale = Math.max(factor * Timeline.MAX_HORIZONTAL_SCALE, Timeline.MIN_HORIZONTAL_SCALE);
        timelineController.getTimeline().setHorizontalScale(scale);
    }

    // MODIFIES: this, timeline singleton
    // EFFECTS: handles behavior for when song ends: changes play icon, stops tickUpdateTimer, brings position to 0
    private void handleEnd() {
        setPlayIcon(playImage);
        timelineController.getTimeline().getPlayer().setPositionTick(0);
        tickUpdateTimer.stop();
    }

    // MODIFIES: timeline singleton
    // EFFECTS: triggers an update to the positionTick field in the instance (and hence fires propertyChangeEvent)
    private void updateTimelineTick() {
        timelineController.getTimeline().getPlayer().updatePositionTick();
    }

    // EFFECTS: listens for timer and button actions and runs methods accordingly
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(play)) {
            togglePlay();
        } else if (source.equals(tickUpdateTimer)) {
            updateTimelineTick();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source.equals(scaleSlider)) {
            updateScale();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch (propertyName) {
            case "timelineReplaced":
                copyTimelineScaleValue();
                break;
            case "playbackEnded":
                handleEnd();
            default:
                break;
        }
    }
}
