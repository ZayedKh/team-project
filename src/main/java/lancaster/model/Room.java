package lancaster.model;

public class Room {

    private int roomID;
    private String name;
    private String roomType;
    private int capacity;
    private int classroomCapacity;
    private int boardroomCapacity;
    private int presentationCapacity;


    /**
     * Constructor for room
     * @param roomID                ID for the room
     * @param name                  Name of the room
     * @param roomType              Type of the room i.e. event, meeting or rehearsal
     * @param capacity              Full capacity of event room
     * @param classroomCapacity     Classroom capacity of meeting room
     * @param boardroomCapacity     Boardroom capacity of meeting room
     * @param presentationCapacity  Presentation capacity of meeting room
     */
    public Room(int roomID, String name, String roomType, int capacity,
                int classroomCapacity, int boardroomCapacity, int presentationCapacity){

        this.roomID = roomID;
        this.name = name;
        this.roomType = roomType;
        this.capacity = capacity;
        this.classroomCapacity = classroomCapacity;
        this.boardroomCapacity = boardroomCapacity;
        this.presentationCapacity = presentationCapacity;

    }

    public int getRoomID() {
        return roomID;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getRoomType() {
        return roomType;
    }
}
