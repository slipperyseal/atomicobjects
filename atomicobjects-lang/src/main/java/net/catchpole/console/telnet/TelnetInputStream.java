package net.catchpole.console.telnet;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A filtered InputStream that handles Telnet Protocol negotations.
 * The read methods return bytes from the stream with Telnet Commands handled
 * and removed. This class requires access to the OutputStream for the Telnet
 * connection so that it may negotiate and respond to peer commands.
 */

public class TelnetInputStream extends FilterInputStream {
    private static final int SE = 240;   // End of subnegotiation parameters.
    private static final int NOP = 241;  // No operation
    private static final int DM = 242;   // Data mark. Indicates the position of a Synch event within the data stream. This should always be accompanied by a TCP urgent notification.
    private static final int BRK = 243;  // Break. Indicates that the "break" or "attention" key was hit.
    private static final int IP = 244;   // Suspend, interrupt or abort the process to which the NVT is connected.
    private static final int AO = 245;   // Abort compiler. Allows the current process to run to completion but do not send its compiler to the user.
    private static final int AYT = 246;  // Are you there. Send back to the NVT some visible evidence that the AYT was received.
    private static final int EC = 247;   // Erase character. The receiver should delete the last preceding undeleted character from the data stream.
    private static final int EL = 248;   // Erase line. Delete characters from the data stream back to but not including the previous CRLF.
    private static final int GA = 249;   // Go ahead. Used, under certain circumstances, to tell the other end that it can transmit.
    private static final int SB = 250;   // Subnegotiation of the indicated option follows.
    private static final int WILL = 251; // Indicates the desire to begin performing, or confirmation that you are now performing, the indicated option.
    private static final int WONT = 252; // Indicates the refusal to perform, or continue performing, the indicated option.
    private static final int DO = 253;   // Indicates the request that the other party perform, or confirmation that you are expecting the other party to perform, the indicated option.
    private static final int DONT = 254; // Indicates the demand that the other party close performing, or confirmation that you are no longer expecting the other party to perform, the indicated option.
    private static final int IAC = 255;  // Interpret as command

    private static final int ECHO = 1;                  // RFC 857
    private static final int SUPPRESSGOAHEAD = 3;       // RFC 858
    private static final int STATUS = 5;                // RFC 859
    private static final int TIMINGMARK = 6;            // RFC 860
    private static final int TERMINALTYPE = 24;         // RFC 1091
    private static final int WINDOWSIZE = 31;           // RFC 1073
    private static final int TERMINALSPEED = 32;        // RFC 1079
    private static final int REMOTEFLOWCONTROL = 33;    // RFC 1372
    private static final int LINEMODE = 34;             // RFC 1184
    private static final int ENVIRONMENTVARIABLES = 36; // RFC 1408

    private static final String[] commandNames = {
            "SE", "NOP", "DM", "BRK", "IP", "AO", "AYT", "EC", "EL", "GA", "SB", "WILL", "WONT", "DO", "DONT", "IAC"
    };
    private static final String[] optionNames = {
            "ECHO", null, "SUPPRESSGOAHEAD", null, "STATUS",    // 1-5
            "TIMINGMARK", null, null, null, null,               // 6-10
            null, null, null, null, null,                       // 11-15
            null, null, null, null, null,                       // 16-20
            null, null, null, "TERMINALTYPE", null,             // 21-25
            null, null, null, null, null,                       // 26-30
            "WINDOWSIZE", "TERMINALSPEED", "REMOTEFLOWCONTROL", "LINEMODE", null,   // 31-35
            "ENVIRONMENTVARIABLES"                              // 36
    };

    /**
     * Client should do local echo.
     */
    private Trace trace = Core.getTrace(this);
    private boolean echo = false;
    private OutputStream outputStream;
    private TelnetEvents telnetEvents;

    public TelnetInputStream(InputStream inputStream, OutputStream outputStream, TelnetEvents telnetEvents) {
        this(inputStream, outputStream);
        this.telnetEvents = telnetEvents;
    }

    public TelnetInputStream(InputStream inputStream, OutputStream outputStream) {
        super(inputStream);
        this.outputStream = outputStream;
    }

    public void negotiate() throws IOException {
        sendCommand(echo ? DO : DONT, ECHO);
        sendCommand(DO, WINDOWSIZE);
        sendCommand(DO, TERMINALTYPE);
        sendSubNegotiation(TERMINALTYPE, 1);
    }

    public int read() throws IOException {
        int value;
        while ((value = attemptReadCommand()) == IAC) {
        }
        return value;
    }

    /**
     * Processes one command returning IAC (255) or returns
     * the read byte if no IAC was found.
     *
     * @return IAC (255) or the read byte if no IAC was found.
     * @throws IOException
     */
    private int attemptReadCommand() throws IOException {
        int value = (super.read() & 0xff);
        if (value != IAC) {
            trace.info(" > ", value);
            return value;
        }
        int command = readCommand();

        int option;
        switch (command) {
            case SB:
                subNegotiation();
                break;
            case WILL:
                option = readOption();
                break;
            case WONT:
                option = readOption();
                break;
            case DO:
                option = readOption();
                break;
            case DONT:
                option = readOption();
                break;
            default:
                return command; // cant interpret
        }
        return value;
    }

    private void subNegotiation() throws IOException {
        int option = readOption();
        switch (option) {
            case WINDOWSIZE:
                int windowWidth = readShort();
                int windowHeight = readShort();
                this.readCommand();  // get IAC
                this.readCommand();  // get SE

                if (this.telnetEvents != null) {
                    this.telnetEvents.setWindowSize(windowWidth, windowHeight);
                }
                break;
            case TERMINALTYPE:
                this.readByte();
                String terminalType = readString();

                if (this.telnetEvents != null) {
                    this.telnetEvents.setTerminalType(terminalType);
                }
                break;
            default:
                trace.warning("subNegotiation wont handle" + getOptionName(option));
        }
    }

    private void sendCommand(int command, int option) throws IOException {
        trace.info("Send", getCommandName(command), getOptionName(option));
        outputStream.write(IAC);
        outputStream.write(command);
        outputStream.write(option);
        outputStream.flush();
    }

    private void sendCommand(int command) throws IOException {
        trace.info("Send", getCommandName(command));
        outputStream.write(IAC);
        outputStream.write(command);
        outputStream.flush();
    }

    private void sendSubNegotiation(int option, int value) throws IOException {
        outputStream.write(IAC);
        outputStream.write(SB);
        outputStream.write(option);
        outputStream.write(value);
        outputStream.write(IAC);
        outputStream.write(SE);
        outputStream.flush();
    }

    private int readCommand() throws IOException {
        int command = (super.read() & 0xff);
        trace.info("command", Integer.toHexString(command), getCommandName(command));
        return command;
    }

    private int readOption() throws IOException {
        int option = (super.read() & 0xff);
        trace.info("options", Integer.toHexString(option), getOptionName(option));
        return option;
    }

    private int readByte() throws IOException {
        int value = (super.read() & 0xff);
        trace.info("value", Integer.toHexString(value & 0xff));
        return value;
    }

    private String readString() throws IOException {
        int value;
        StringBuilder sb = new StringBuilder(32);
        while ((value = readByte()) != IAC) {
            sb.append((char) value);
        }
        String string = sb.toString();
        trace.info(string);
        readByte(); // read SE
        return string;
    }

    private int readShort() throws IOException {
        int value = ((super.read() & 0xff) << 8) | (super.read() & 0xff);
        trace.info("short", Integer.toHexString(value));
        return value;
    }

    private static String getCommandName(int command) {
        if (command >= 240 && command <= 255) {
            return commandNames[command - 240];
        }
        return null;
    }

    private static String getOptionName(int option) {
        if (option >= 1 && option <= 36) {
            return optionNames[option - 1];
        }
        return null;
    }
}
