package com.example.ziadartwork.ui


//@OptIn(ExperimentalLifecycleComposeApi::class)
//@Composable
//fun ZiadPaintingsApp(
//    navigate: NavController,
//    modifier: Modifier = Modifier,
//) {
//    Scaffold(
//        modifier = modifier.fillMaxSize(),
//        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
//    ) {
//        Surface(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(it),
//            color = MaterialTheme.colors.background
//        ) {
//            val viewModel: MainActivityViewModel = hiltViewModel()
//            val homeScreenState by remember(viewModel) {
//                viewModel.fetchPaintings
//            }.collectAsStateWithLifecycle(Result.Loading)
//
////            PaintingsHomeScreen(
////                paintingsScreenState = homeScreenState,
////                retryAction = { },
////                onPaintingSelected = { navController.navigate("detail/${it.name}") })
////            )
//        }
//    }
//
//
//}


//fun navigateTo(navigate: NavController, id: String) {
//    navigate.navigate(Destination.DetailDestination.withArgs(id))
//}