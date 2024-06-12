import javax.sound.midi.*;

public class MidiNoteReader {
    public static void main(String[] args) {
        try {
            // Get the default MIDI device
            MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
            MidiDevice device = null;
            
            // Find and open a suitable MIDI device
            for (MidiDevice.Info info : infos) {
                device = MidiSystem.getMidiDevice(info);
                if (device.getMaxTransmitters() != 0) {
                    device.open();
                    break;
                }
            }
            
            if (device == null || !device.isOpen()) {
                System.out.println("No suitable MIDI device found or couldn't open the device.");
                return;
            }

            // Get the transmitter from the device
            Transmitter transmitter = device.getTransmitter();
            
            // Create a receiver to listen for MIDI messages
            Receiver receiver = new Receiver() {
                @Override
                public void send(MidiMessage message, long timeStamp) {
                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        if (sm.getCommand() == ShortMessage.NOTE_ON) {
                            int key = sm.getData1();
                            int velocity = sm.getData2();
                            if (velocity > 0) { // Only consider note-on messages with non-zero velocity
                                System.out.println("Note on: " + key);
                            }
                        }
                    }
                }

                @Override
                public void close() {
                }
            };

            // Attach the receiver to the transmitter
            transmitter.setReceiver(receiver);

            // Keep the program running to listen for MIDI input
            System.out.println("Listening for MIDI input. Press Ctrl+C to exit.");
            Thread.sleep(Long.MAX_VALUE);

        } catch (MidiUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
} I'm