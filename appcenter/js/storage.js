/*! session storage(문자형만 저장가능)
 *  브라우저종료시 초기화
 */

/**
 * 세션 설정(오브젝트는 JSON.stringify(값) 로 변환해야한다)
 * @param key
 * @param value
 */
function setSession(key, value){
	sessionStorage.setItem(key, value);
}

/**
 * 세션 취득(오브젝트는 JSON.parse(값) 로 변환해야한다)
 * @param key
 * @returns session
 */
function getSession(key){
	return sessionStorage.getItem(key);
}

/**
 * 세션 길이
 * @returns length
 */
function getSessionLength(){
	return sessionStorage.length;
}

/**
 * 세션 취득(순번)
 * @param index
 * @returns session
 */
function getSessionIndex(index){
	return sessionStorage.key(index);
}

/**
 * 세션 제거
 * @param key
 */
function removeSession(key){
	sessionStorage.removeItem(key);
}

/**
 * 세션 전체 제거
 */
function clearSession(){
	sessionStorage.clear();
}


/*! local storage(문자형만 저장가능)
 * 영구 보존
 */

/**
 * 로컬 설정(오브젝트는 JSON.stringify(값) 로 변환해야한다)
 * @param key
 * @param value
 */
function setLocal(key, value){
	localStorage.setItem(key, value);
}

/**
 * 로컬 취득(오브젝트는 JSON.parse(값) 로 변환해야한다)
 * @param key
 * @returns local
 */
function getLocal(key){
	return localStorage.getItem(key);
}

/**
 * 로컬 길이
 * @returns length
 */
function getLocalLength(){
	return localStorage.length;
}

/**
 * 로컬 취득(순번)
 * @param index
 * @returns local
 */
function getLocalIndex(index){
	return localStorage.key(index);
}

/**
 * 로컬 제거
 * @param key
 */
function removeLocal(key){
	localStorage.removeItem(key);
}

/**
 * 로컬 전체 제거
 */
function clearLocal(){
	localStorage.clear();
}