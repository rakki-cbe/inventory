package rakki.inventory.basic

import android.arch.persistence.room.*
import java.util.*

sealed class Entities {

    @Entity(tableName = "UserInfo", indices = [Index("userName")])
    data class UserDetails(
        var userName: String? = null,
        var userPassword: String? = null,
        var userRole: String = "",
        var userFullName: String? = null,
        var ivInfo: String? = null
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null
    }

    @Entity(tableName = "category", indices = [Index("categoryName")])
    data class Category(
        var categoryName: String, var cateDescription: String,
        @TypeConverters(TimeStampConverter::class) var createdDate: Date? = Calendar.getInstance().time
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null
    }

    @Entity(
        tableName = "SubCategory", indices = [Index("subCategoryName")]
        , foreignKeys = [ForeignKey(
            entity = Category::class, parentColumns = ["id"], childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )]
    )
    data class SubCategory(
        var categoryId: Long, var subCategoryName: String, var description: String,
        @TypeConverters(TimeStampConverter::class) var createdDate: Date? = Calendar.getInstance().time
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null
    }

    @Entity(tableName = "Brand", indices = [Index("brandName")])
    data class Brand(
        var brandName: String, var description: String,
        @TypeConverters(TimeStampConverter::class) var createdDate: Date? = Calendar.getInstance().time
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null
    }

    @Entity(
        tableName = "Product",
        indices = [Index(
            value = ["productName", "categoryId", "subcategoryId", "amount", "brandId", "unitQuantity", "saleAmount"],
            unique = true
        )]
        , foreignKeys = [ForeignKey(
            entity = Category::class, parentColumns = ["id"], childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ), ForeignKey(
            entity = SubCategory::class, parentColumns = ["id"],
            childColumns = ["subcategoryId"], onDelete = ForeignKey.CASCADE
        ), ForeignKey(
            entity = Brand::class,
            parentColumns = ["id"], childColumns = ["brandId"], onDelete = ForeignKey.CASCADE
        )]
        // primaryKeys = ["categoryId", "subcategoryId", "amount", "brandId", "unitQuantity", "saleAmount", "productCode"]
    )
    data class Product(
        var categoryId: Long,
        var subcategoryId: Long,
        var brandId: Long,
        var productCode: String,
        var productName: String,
        var unitQuantity: Double = 0.0,
        var unitQuantityKey: String = "",
        var amount: Double = 0.0,
        var saleAmount: Double = 0.0,
        var noOfUnit: Double = 0.0,
        var description: String,
        var locationHint: String,
        @TypeConverters(TimeStampConverter::class) var createdDate: Date? = Calendar.getInstance().time
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null
    }


}