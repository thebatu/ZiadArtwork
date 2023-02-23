import com.example.ziadartwork.Result
import com.example.ziadartwork.fake.FakeDataSource
import com.example.ziadartwork.model.Painting
import com.example.ziadartwork.model.PaintingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNetworkPaintingRepository : PaintingsRepository {
    override fun getAllPaintings(): Flow<Result<List<Painting>>> {
        val data: com.example.ziadartwork.Response.Result.Success<List<Painting>> = FakeDataSource.paintingsMock
        return flow { Result.Success(data) }
    }

}

