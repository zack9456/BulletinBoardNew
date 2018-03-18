package Data;

public interface BaseBroker {

    DTO[] getAll(DTO dto, DTO[] returnArray);

    DTO getSingle(DTO dto);

    DTO save(DTO dto);
}
