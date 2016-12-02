BRANCH_NAME = "${env.BRANCH_NAME}"
MAJOR_VERSION = "0"
MINOR_VERSION = "0"

VERSION_NUMBER = ""

node('master') {
    stage('COMMIT') {
        cleanWorkspace()
        VERSION_NUMBER = determineVersionNumber()
        git branch: BRANCH_NAME, credentialsId: 'github', url: 'git@github.com:cegeka/tether.git'
    }
}

stage('Do you want to release?') {
    milestone label: 'confirm release'
    input 'Do you want to release?'
    milestone label: 'release'
}

node('master') {
    stage('RELEASE') {
        cleanWorkspace()
        git branch: BRANCH_NAME, credentialsId: 'github', url: 'git@github.com:cegeka/tether.git'
    }
}

/********** library **********/

def cleanWorkspace() {
    sh "rm -rf *"
}

def determineVersionNumber() {

    def incrementalVersion = determineIncrementalVersion()
    def qualifier = determineVersionQualifier()

    def version = "${MAJOR_VERSION}.${MINOR_VERSION}.${incrementalVersion}${qualifier}"
    println "Building with version number: ${version}";
    return version
}

def determineIncrementalVersion() {
   def suffix = sh returnStdout: true, script: 'git rev-list --count HEAD'
   return suffix.trim()
}

def determineVersionQualifier() {
    if (BRANCH_NAME.equalsIgnoreCase("master")) {
        return ""
    } else {
        return "-" + BRANCH_NAME
    }
}

