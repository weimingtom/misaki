package com.iteye.weimingtom.tgui.sf;

public class Style {
    public final static int None       = 0;      ///< No border / title bar (this flag and all others are mutually exclusive)
    public final static int Titlebar   = 1 << 0; ///< Title bar + fixed border
    public final static int Resize     = 1 << 1; ///< Titlebar + resizable border + maximize button
    public final static int Close      = 1 << 2; ///< Titlebar + close button
    public final static int Fullscreen = 1 << 3; ///< Fullscreen mode (this flag and all others are mutually exclusive)

    public final static int Default = Titlebar | Resize | Close;///< Default window style
}
