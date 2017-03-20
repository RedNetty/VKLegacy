package me.kayaba.guilds.api.util.exceptionparser;

import java.util.*;

public interface ErrorSignature {

    String toString();


    Collection<Block> getBlocks();


    void addBlock(Block... block);
}
