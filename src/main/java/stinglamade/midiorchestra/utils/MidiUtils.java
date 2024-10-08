package stinglamade.midiorchestra.utils;

import javax.sound.midi.*;

public class MidiUtils {

    public static Synthesizer synthesizer;

    static {
        try {
            synthesizer = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    static int instrumentNr;
    static Instrument[] instruments = synthesizer.getDefaultSoundbank().getInstruments();
    static MidiChannel channel;
    static int pitch = 60;
    static int velocity = 100;
    static Receiver receiver;

    static {
        try {
            receiver = synthesizer.getReceiver();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    static ShortMessage noteOn;
    static ShortMessage noteOff;
    static int duration = 1000;
    static int channelNr = 0;

    public static void playMidi (int instrumentNumber) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {

        if (instrumentNumber >= 0 && instrumentNumber < 235) {
            instrumentNr = instrumentNumber;
        }

        synthesizer.open();

        // Wähle ein Instrument aus (z.B. das erste Instrument)
        //synthesizer.loadInstrument(instruments[1]);

        // Hole den ersten MIDI-Kanal
        MidiChannel[] channels = synthesizer.getChannels();
        channel = channels[0];

        // Setze das Instrument auf dem Kanal
        channel.programChange(instruments[instrumentNr].getPatch().getProgram());

        noteOn = createShortmessage(channelNr, pitch, velocity);
        noteOff = createShortmessageOff(channelNr, pitch, velocity);
        receiver.send(noteOn, -1);

        Thread.sleep(duration);

        receiver.send(noteOff, -1);

        synthesizer.close();
    }

    public int getInstrumentNumber() {
        return instrumentNr;
    }

    public static ShortMessage createShortmessage(int channelNr, int pitch, int velocity) throws InvalidMidiDataException {
        ShortMessage noteOn = new ShortMessage(ShortMessage.NOTE_ON, channelNr, pitch, velocity);
        receiver.send(noteOn, -1);
        return noteOn;
    }

    public static ShortMessage createShortmessageOff(int channelNr, int pitch, int velocity) throws InvalidMidiDataException {
        ShortMessage noteOn = new ShortMessage(ShortMessage.NOTE_OFF, channelNr, pitch, velocity);
        receiver.send(noteOn, -1);
        return noteOn;
    }
}