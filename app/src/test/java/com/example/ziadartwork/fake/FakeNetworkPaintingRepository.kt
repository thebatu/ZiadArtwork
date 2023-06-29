import com.example.ziadartwork.ui.Result
import com.example.ziadartwork.fake.FakeDataSource
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.domain.repository.PaintingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNetworkPaintingRepository : PaintingsRepository {
    override fun getAllPaintings(): Flow<Result<List<Painting>>> {
        val data: Result.Success<List<Painting>> = FakeDataSource.paintingsMock
        return flow { Result.Success(data) }
    }

    override suspend fun getPainting(id: String): Result<Painting> {
        TODO("Not yet implemented")
    }

}

