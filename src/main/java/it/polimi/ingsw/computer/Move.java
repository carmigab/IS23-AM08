package it.polimi.ingsw.computer;

import it.polimi.ingsw.model.Position;

import java.util.List;

public record Move(List<Position> positions, int column) {

}
