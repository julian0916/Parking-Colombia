package com.example.zer.somos.utilidades.textparser;

import com.example.zer.somos.utilidades.EscPosPrinterCommands;
import com.example.zer.somos.utilidades.exceptions.EscPosConnectionException;
import com.example.zer.somos.utilidades.exceptions.EscPosEncodingException;

public interface IPrinterTextParserElement {
    int length() throws EscPosEncodingException;
    IPrinterTextParserElement print(EscPosPrinterCommands printerSocket) throws EscPosEncodingException, EscPosConnectionException;
}
