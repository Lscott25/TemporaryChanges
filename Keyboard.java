import javax.sound.midi.*;

public class MidiKeyboardListener {

    public static void main(String[] args) {
        try {
            // Get available MIDI devices
            MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
            MidiDevice midiDevice = null;

            // Find the first MIDI input device
            for (MidiDevice.Info info : midiDeviceInfo) {
                midiDevice = MidiSystem.getMidiDevice(info);
                if (midiDevice.getMaxTransmitters() != 0) {
                    break;
                }
            }

            if (midiDevice == null) {
                System.out.println("No MIDI input device found.");
                return;
            }

            // Open the MIDI device
            midiDevice.open();

            // Create a receiver to handle MIDI messages
            Receiver midiReceiver = new MidiReceiver();
            Transmitter transmitter = midiDevice.getTransmitter();
            transmitter.setReceiver(midiReceiver);

            System.out.println("Listening for MIDI messages. Press Ctrl+C to quit.");

            // Keep the program running to listen for MIDI messages
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Custom receiver to handle MIDI messages
    public static class MidiReceiver implements Receiver {
        private Synthesizer synthesizer;
        private MidiChannel[] channels;

        public MidiReceiver() {
            try {
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
                channels = synthesizer.getChannels();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;
                int command = sm.getCommand();
                int channel = sm.getChannel();
                int note = sm.getData1();
                int velocity = sm.getData2();

                switch (command) {
                    case ShortMessage.NOTE_ON:
                        if (velocity > 0) {
                            channels[channel].noteOn(note, velocity);
                            System.out.println("Note On: " + note + " Velocity: " + velocity);
                        } else {
                            channels[channel].noteOff(note);
                            System.out.println("Note Off: " + note);
                        }
                        break;
                    case ShortMessage.NOTE_OFF:
                        channels[channel].noteOff(note);
                        System.out.println("Note Off: " + note);
                        break;
                }
            }
        }

        @Override
        public void close() {
            synthesizer.close();
        }
    }
}