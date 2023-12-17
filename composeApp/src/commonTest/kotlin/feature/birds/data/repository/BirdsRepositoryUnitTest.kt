package feature.birds.data.repository

import arrow.core.right
import feature.birds.data.datasource.BirdsRemoteDataSource
import feature.birds.data.datasource.MockBirdsRemoteDataSource
import feature.birds.data.model.BirdImage
import feature.birds.data.model.GetBirdsError
import infrastructure.core.data.datasource.DataException
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.ktor.http.parsing.ParseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
@UsesMocks(BirdsRemoteDataSource::class)
internal class BirdsRepositoryUnitTest : FunSpec() {
    init {
        coroutineTestScope = true
        val mocker = Mocker()

        context("GIVEN a birds repository") {
            val standardTestDispatcher = StandardTestDispatcher(testCoroutineScheduler)
            val birdsRemoteDataSourceMock: BirdsRemoteDataSource = MockBirdsRemoteDataSource(mocker)
            val repository = BirdsRepositoryImpl(birdsRemoteDataSourceMock, standardTestDispatcher)

            test("WHEN get birds succeeds THEN result should be a list of birds") {
                mocker.everySuspending { birdsRemoteDataSourceMock.getBirdImages() } returns listOf(birdOne, birdTwo)

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
        val birdOne = BirdImage(
            author = "author1",
            category = "category1",
            path = "path1",
        )
        val birdTwo = BirdImage(
            author = "author2",
            category = "category2",
            path = "path2",
        )
    }
}
