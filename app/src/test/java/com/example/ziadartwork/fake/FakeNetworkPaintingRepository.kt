import android.util.Log
import com.example.ziadartwork.Response
import com.example.ziadartwork.fake.FakeDataSource
import com.example.ziadartwork.model.Painting
import com.example.ziadartwork.model.PaintingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNetworkPaintingRepository : PaintingsRepository {
    override fun getAllPaintings(): Flow<Response<List<Painting>>> {
        val data: Response.Success<List<Painting>> = FakeDataSource.paintingsMock
        return flow { Response.Success(data) }
    }

}

