package feature.birds.data.repository

import arrow.core.right
import feature.birds.data.datasource.BirdsRemoteDataSource
import feature.birds.data.datasource.MockBirdsRemoteDataSource
import feature.birds.data.model.BirdImage
import feature.birds.data.model.GetBirdsError
import feature.birds.ui.model.Bird
import infrastructure.core.data.datasource.DataException
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.parsing.ParseException
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(BirdsRemoteDataSource::class)
private class BirdsRepositoryUnitTest : FunSpec() {
    init {
        coroutineTestScope = true
        val mocker = Mocker()

        context("GIVEN a birds repository") {
            val birdsRemoteDataSourceMock: BirdsRemoteDataSource = MockBirdsRemoteDataSource(mocker)
            val repository = BirdsRepositoryImpl(birdsRemoteDataSourceMock)

            test("WHEN get birds succeeds THEN result should be a list of birds") {
                mocker.everySuspending { birdsRemoteDataSourceMock.getBirdImages() } returns listOf(birdImageOne, birdImageTwo)

                repository.getBirds() shouldBe listOf(birdOne, birdTwo).right()

                mocker.verifyWithSuspend {
                    birdsRemoteDataSourceMock.getBirdImages()
                }
            }

            test("WHEN get birds fails with data exception THEN result should network error") {
                mocker.everySuspending { birdsRemoteDataSourceMock.getBirdImages() } runs { throw DataException.Network(400, "") }

                repository.getBirds() shouldBe GetBirdsError.Network.right()

                mocker.verifyWithSuspend {
                    birdsRemoteDataSourceMock.getBirdImages()
                }
            }

            test("WHEN get birds fails THEN result should unknown error") {
                mocker.everySuspending { birdsRemoteDataSourceMock.getBirdImages() } runs { throw ParseException("") }

                repository.getBirds() shouldBe GetBirdsError.Unknown.right()

                mocker.verifyWithSuspend {
                    birdsRemoteDataSourceMock.getBirdImages()
                }
            }
        }
    }

    companion object {
        private const val AUTHOR_ONE = "author1"
        private const val AUTHOR_TWO = "author2"
        private const val CATEGORY_ONE = "category1"
        private const val CATEGORY_TWO = "category2"
        private const val PATH_ONE = "path1"
        private const val PATH_TWO = "path2"
        private val birdImageOne = BirdImage(
            author = AUTHOR_ONE,
            category = CATEGORY_ONE,
            path = PATH_ONE,
        )
        private val birdImageTwo = BirdImage(
            author = AUTHOR_TWO,
            category = CATEGORY_TWO,
            path = PATH_TWO,
        )
        private val birdOne = Bird(
            author = AUTHOR_ONE,
            category = CATEGORY_ONE,
            path = PATH_ONE,
        )
        private val birdTwo = Bird(
            author = AUTHOR_TWO,
            category = CATEGORY_TWO,
            path = PATH_TWO,
        )
    }
}
