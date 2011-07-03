/*
 * Copyright 2009 Peter Geirnaert
 *
 * This file is part of JSynthLib.
 *
 * JSynthLib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * JSynthLib is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JSynthLib; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package synthdrivers.YamahaCS2x;

import core.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author peter
 */
public class YamahaCS2xCommonEditor extends PatchEditorFrame {
    
    public YamahaCS2xCommonEditor(Patch p) {
        super("Common Editor for CS2x Performance",p);
        scrollPane.setLayout(new GridLayout(0,1));
        addCommonPane(p);
    }
    public void addCommonPane(Patch p) {
        JPanel commonPane = new JPanel();
        commonPane.setLayout(new GridBagLayout());
        
        //alles hieronder moet nog aangepast worden voor common data ipv layerdata ..
        addWidget(commonPane, // the parent JComponent
                new ScrollBarWidget( // the type of SysexWidget to add
                "Volume", // label
                p, 0, 127, // patch, min, max
                0,// base value. This value is added to the actual value for display purposes
                new CommonModel(p,16),new CommonSender(p,7)),
                0, 1, 2, 1, 2); //  
    }
    class CommonModel implements SysexWidget.IParamModel {
    /** <code>Patch</code> data. */
    protected Patch patch;
    /** Offset of the data for which this model is. */
    protected int ofs;

        public CommonModel(Patch p, int ofs) {
            this.patch = p;
            this.ofs = ofs;
        }
        public void set(int value) {
            patch.sysex[ofs] = (byte)value;
        }

        public int get() {
            return patch.sysex[ofs];
        }
        
    }
    class CommonSender extends SysexSender {
        Patch patch;
        byte[] sysx;
        int ofset;
        int lyr;
        public CommonSender(Patch p, int offset){
            //      0 1 2 3 4 5 6 7 8
            super("F043@@63600110**F7");
            this.patch = p;
            this.ofset = offset;
            this.sysx = patch.sysex;
            this.lyr = sysx[7];
        }
        public byte[] generate(int value) {
            byte[] syse = super.generate(value);
            int dev = syse[2];
            syse[2]=(byte)(dev+0x10);
            syse[5]=(byte)lyr;
            syse[6]=(byte)ofset;
            return syse;
        }
    }
}
