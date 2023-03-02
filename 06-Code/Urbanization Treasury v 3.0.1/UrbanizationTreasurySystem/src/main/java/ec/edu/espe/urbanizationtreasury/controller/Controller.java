package ec.edu.espe.urbanizationtreasury.controller;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import ec.edu.espe.urbanizationtreasury.model.Payment;
import ec.edu.espe.urbanizationtreasury.model.Resident;
import java.util.Scanner;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author Rivera Joel, WebMasterTeam, DCCO-ESPE
 */
public class Controller extends javax.swing.JFrame{

    private static final Scanner sc = new Scanner(System.in);
    private static final Resident resident = new Resident();
    
    

    public static boolean noRepeatRecident(MongoDatabase database, Resident resident, boolean existResident) {

        MongoCollection<Document> collection2 = database.getCollection("Residents");
        Bson filter = Filters.and(Filters.all("id", resident.getId()));

        if (collection2.find(filter).first() == null) {
            existResident = false;
        }
        if (collection2.find(filter).first() != null) {
            existResident = true;
        }
        return existResident;

    }

    public static String readMongo(MongoDatabase database, Resident resident, String month) {
        
        String document = "";
        Gson gson = new Gson();
        
        MongoCollection<Document> collection = database.getCollection("Residents");
        
        Bson filter = Filters.eq("id", resident.getId());
        Document query = collection.find(Filters.and(filter)).first();

        document = query.toJson();
        TypeToken<Payment> type = new TypeToken<Payment>() {
        };
        Payment paymentType = gson.fromJson(document, type.getType());

        month = (paymentType.getMonth());
        
        return month;
        
    }
    
    public static void enterPayments(MongoDatabase database, Payment payment, Payment paymentTypeSelect) {

        MongoCollection<Document> collection = database.getCollection(paymentTypeSelect.getPaymentType());
        
        System.out.println("---------------------------------");
        System.out.println("" + resident.getId());
        
        Document inspection = new Document("_id", new ObjectId())
                .append("id", payment.getIdPayment())
                .append("month", payment.getMonth())
                .append("payment", payment.getPayment());

        collection.insertOne(inspection);
    }

}
