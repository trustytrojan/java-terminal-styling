package socket_chat;

public final class TerminalStyling {

	// the ESC character can be represented in Java as (char)27 or "\u001b"
	private static final String SEQUENCE_START = "\u001b[";
	private static final String RESET = (SEQUENCE_START + "0m");

	private static class SequenceNotBuiltException extends Exception {

		private SequenceNotBuiltException() {
			super("You must call build() on this StyleSequence before using it to style or print strings!");
		}

	}

	public static class StyleSequence {

		private Style style;
		private Color fgColor;
		private Color bgColor;
		private boolean fgBright;
		private boolean bgBright;

		// this holds the final escape sequence, set by build()
		private String sequence;

		public StyleSequence() {}

		// This constructor will build the sequence for you
		public StyleSequence(Style style, Color fgColor, boolean fgBright, Color bgColor, boolean bgBright) {
			this.style = style;
			this.fgColor = fgColor;
			this.fgBright = fgBright;
			this.bgColor = bgColor;
			this.bgBright = bgBright;
			build();
		}

		public void setStyle(Style style) {
			this.style = style;
		}

		public void setForegroundColor(Color color) {
			fgColor = color;
		}

		public void setForegroundBright(boolean bright) {
			fgBright = bright;
		}

		public void setBackgroundColor(Color color) {
			bgColor = color;
		}

		public void setBackgroundBright(boolean bright) {
			bgBright = bright;
		}

		public void build() {
			var seq = SEQUENCE_START;
			if (style != null)
				seq += style.value;
			if (fgColor != null) {
				seq += ';';
				if (fgBright)
					seq += fgColor.fgBright;
				else
					seq += fgColor.fg;
			}
			if (bgColor != null) {
				seq += ';';
				if (bgBright)
					seq += bgColor.bgBright;
				else
					seq += bgColor.bg;
			}
			sequence = (seq += 'm');
		}

		private void nullCheck() throws SequenceNotBuiltException {
			if (sequence == null)
				throw new SequenceNotBuiltException();
		}

		public String style(Object o) throws SequenceNotBuiltException {
			nullCheck();
			return (sequence + o + RESET);
		}

		public void println(Object o) throws SequenceNotBuiltException {
			nullCheck();
			System.out.println(style(o));
		}

		public void print(Object o) throws SequenceNotBuiltException {
			nullCheck();
			System.out.print(style(o));
		}

	}

	public static enum Color {

		Black(0),
		Red(1),
		Green(2),
		Yellow(3),
		Blue(4),
		Magenta(5),
		Cyan(6),
		White(7),
		Default(9);

		private final String fg;
		private final String bg;
		private final String fgBright;
		private final String bgBright;
		
		private Color(int number) {
			fg = "3" + number;
			bg = "4" + number;
			fgBright = "9" + number;
			bgBright = "10" + number;
		}

	}

	public static enum Style {

		Bold(1),
		Dim(2),
		Italic(3),
		Underline(4),
		Blinking(5),
		Inverse(6),
		Invisible(7),
		Strikethrough(8),
		DoubleUnderline(21);

		private final short value;

		private Style(int value) {
			this.value = (short)value;
		}

	}

	private TerminalStyling() {}

}
