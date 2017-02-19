package smart.shared;

public class DataMapKeys {
    public static final String ACCURACY = "accuracy";
    public static final String TIMESTAMP = "timestamp";
    public static final String Data_RATE="data_rate";
    public static final String VALUES = "values";
    public static final String FILTER = "filter";
    public static final String FILTER_SWITCH = "filter_switch";
    public static final String SEQUENCE_NUMBER="sequence_number";

    public static final String DETECTION="detection";
    public static final String CORRECTNESS="correctness";
    public static final String FILE_COUNT="file_count";
    public static final String FILE_COUNT_INTENT="file_count_intent";

    public static final String REMOTE_MESSAGE="remote_message";

    public static final String IDLENESS="idleness";
    public static final String ACTIVITY_ID="activity_id";

    public static final String USERNAME="user_name";

    public static final String DATAFILE="data_file";
//    public static final String SOUNDFILE="sound_file";
    public static final String DATAFILENAME="data_file_name";
    public static final String DATAFOLDERNAME="data_folder_name";


    public static final String FILE_RECEIVE_UPDATE="FILE_RECEIVE_UPDATE";
    public static final String FILE_UPLOAD_UPDATE="FILE_UPLOAD_UPDATE";

    public static final String TRAINING_LABEL="TRAINING_LABEL";
    public static final String PREDICTED_LABEL="PREDICTED_LABEL";

    public static final String MAG_CALI_FILE="/sdcard/sensor_data/calibrated.csv";


    public static final int SURFACE_BRUSH_TIME_GOAL=8;




    public static final String[] SURFACE_FILE_NAME={"front_buccal_upper","front_buccal_lower",
            "front_lingual_upper","front_lingual_lower",
            "left_buccal_lower","left_chewing_lower","left_lingual_lower",
            "left_buccal_upper","left_chewing_upper","left_lingual_upper",
            "right_buccal_lower","right_chewing_lower","right_lingual_lower",
            "right_buccal_upper","right_chewing_upper","right_lingual_upper","empty"};


    public static final String[] SURFACE_NAME={"Front Up Out","Front Low Out",
            "Front Up In","Front Low In",
            "Left Low Out","Left Low Chew","Left Low In",
            "Left Up Out","Left Up Chew","Left Up In",
            "Right Low Out","Right Low Chew","Right Low In",
            "Right Up Out","Right Up Chew","Right Up In","Other"};


}
